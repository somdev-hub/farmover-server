package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Company;
import com.farmover.server.farmover.entities.CompanyPurchases;
import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.StorageBookings;
import com.farmover.server.farmover.entities.StorageType;
import com.farmover.server.farmover.entities.TransactionType;
import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.entities.WarehouseSales;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.CompanyDto;
import com.farmover.server.farmover.payloads.CompanyPurchasesDto;
import com.farmover.server.farmover.payloads.CompanyWarehouseCardDto;
import com.farmover.server.farmover.payloads.FarmerItem;
import com.farmover.server.farmover.payloads.records.AvailableCropWarehouseCard;
import com.farmover.server.farmover.payloads.records.RegisteredFarmersInfo;
import com.farmover.server.farmover.payloads.request.CompanyRegisterDto;
import com.farmover.server.farmover.repositories.CompanyRepo;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.StorageBookingsRepo;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.CompanyServices;

import jakarta.transaction.Transactional;

@Service
public class CompanyServiceImpl implements CompanyServices {

    @Autowired
    WareHouseRepo wareHouseRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    StorageRepo storageRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    S3ServiceImpl s3Service;

    @Autowired
    CompanyRepo companyRepo;

    @Autowired
    ProductionRepo productionRepo;

    @Autowired
    StorageBookingsRepo storageBookingsRepo;

    @Override
    public List<CompanyWarehouseCardDto> getWarehouseCardDtos() {
        List<Warehouse> warehouses = wareHouseRepo.findAll();
        return warehouses.stream().map(warehouse -> {
            CompanyWarehouseCardDto dto = new CompanyWarehouseCardDto();
            dto.setId(warehouse.getId());
            dto.setName(warehouse.getName());
            dto.setAddress(warehouse.getAddress());
            dto.setWarehouseImage(warehouse.getWarehouseImage());

            List<Crops> availableItems = warehouse.getStorages().stream()
                    .flatMap(storage -> storage.getStorageBookings().stream())
                    .map(storageBooking -> storageBooking.getCropName())
                    .collect(Collectors.toList());

            dto.setAvailableItems(availableItems);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<StorageType, Map<Crops, ArrayList<FarmerItem>>> getWarehouseMarketData(Integer warehouseId) {
        Warehouse warehouse = wareHouseRepo.findById(warehouseId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Warehouse", "warehouse id", warehouseId.toString());
        });

        Map<StorageType, Map<Crops, ArrayList<FarmerItem>>> storageTypeMap = new HashMap<>();

        for (Storage storage : warehouse.getStorages()) {
            for (StorageBookings storageBookings : storage.getStorageBookings()) {

                if (storageBookings.getAvailableQuantity() <= 0) {
                    continue;
                }
                StorageType storageType = storage.getStorageType();
                Crops cropName = storageBookings.getCropName();
                String clientEmail = storageBookings.getClientEmail();

                Map<Crops, ArrayList<FarmerItem>> cropMap = storageTypeMap.computeIfAbsent(storageType,
                        k -> new HashMap<>());
                ArrayList<FarmerItem> farmerItems = cropMap.computeIfAbsent(cropName, k -> new ArrayList<>());

                FarmerItem farmerItem = new FarmerItem();
                farmerItem.setFarmerName(userRepo.findByEmail(clientEmail).orElseThrow(() -> {
                    throw new ResourceNotFoundException("User", "email", clientEmail);
                }).getUname());
                farmerItem.setCrop(cropName);
                farmerItem.setFarmerEmail(clientEmail);
                farmerItem.setFarmerImage(userRepo.findByEmail(clientEmail).orElseThrow(() -> {
                    throw new ResourceNotFoundException("User", "email", clientEmail);
                }).getProfileImage());
                farmerItem.setBookedDate(storageBookings.getBookingDate());
                farmerItem.setUnit(storageBookings.getItemUnit());
                farmerItem.setPrice(storageBookings.getItemPrice());
                farmerItem.setWeight(storageBookings.getAvailableQuantity());
                farmerItem.setProductionToken(storageBookings.getProductionToken());

                farmerItems.add(farmerItem);
            }
        }

        return storageTypeMap;

    }

    @Override
    public List<RegisteredFarmersInfo> getRegisteredFarmers(Integer warehouseId) {
        Warehouse warehouse = wareHouseRepo.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "warehouse id", warehouseId.toString()));

        // List<RegisteredFarmersInfo> infos=new ArrayList<>();

        HashSet<RegisteredFarmersInfo> set = new HashSet<>();

        warehouse.getStorages().forEach(storage -> {
            storage.getStorageBookings().forEach(storageBookings -> {
                User user = userRepo.findByEmail(storageBookings.getClientEmail())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("User", "email", storageBookings.getClientEmail()));
                set.add(new RegisteredFarmersInfo(user.getUname(), user.getEmail(), user.getProfileImage()));
            });
        });

        return set.stream().collect(Collectors.toList());
    }

    @Override
    public CompanyDto registerCompany(CompanyRegisterDto companyRegisterDto, String email) throws IOException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        Company company = modelMapper.map(companyRegisterDto, Company.class);

        company.setManager(user);

        String companyImage = s3Service.uploadFile(companyRegisterDto.getCompanyImage());

        company.setCompanyImage(companyImage);

        company.setCompanyPurchases(new ArrayList<>());

        Company savedCompany = companyRepo.save(company);

        return modelMapper.map(savedCompany, CompanyDto.class);
    }

    @Override
    @Transactional
    public void purchaseItems(Map<Integer, Double> productionTokens, String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        Company company = companyRepo.findByManager(user).orElseThrow(() -> {
            throw new ResourceNotFoundException("Company", "manager", user.getEmail());
        });

        List<CompanyPurchases> companyPurchases = company.getCompanyPurchases();

        productionTokens.forEach((token, quantity) -> {
            StorageBookings storageBooking = storageBookingsRepo.findStorageBookingsByProductionToken(token)
                    .orElseThrow(() -> {
                        throw new ResourceNotFoundException("StorageBookings", "production token", token.toString());
                    });

            CompanyPurchases purchase = new CompanyPurchases();
            purchase.setCompany(company);
            purchase.setCrop(storageBooking.getCropName());
            purchase.setPurchaseDate(LocalDate.now());
            purchase.setPurchaseQuantity(quantity);
            purchase.setPurchasePrice(storageBooking.getItemPrice());
            purchase.setPurchaseTotal(storageBooking.getItemPrice() * quantity);
            purchase.setStatus("PURCHASED");
            purchase.setWarehouseName(storageBooking.getStorage().getWarehouse().getName());

            Warehouse warehouse = storageBooking.getStorage().getWarehouse();
            List<WarehouseSales> warehouseSales = warehouse.getWarehouseSales();

            WarehouseSales sales = new WarehouseSales();
            sales.setBuyer(company.getName());
            sales.setCrop(storageBooking.getCropName());
            sales.setPrice(storageBooking.getItemPrice() * quantity);
            sales.setCommission(storageBooking.getItemPrice() * quantity * 0.05);
            sales.setQuantity(quantity);
            sales.setUnit(storageBooking.getItemUnit());
            sales.setDate(new Date(System.currentTimeMillis()));
            sales.setProductionToken(token);
            sales.setStorageType(storageBooking.getStorage().getStorageType());
            sales.setWarehouse(warehouse);

            warehouseSales.add(sales);

            storageBooking.setStatus("SOLD");
            storageBooking.setAvailableQuantity(storageBooking.getAvailableQuantity() - quantity);

            storageBooking.getStorage().setAvailableCapacity(
                    storageBooking.getStorage().getAvailableCapacity() + (storageBooking.getBookedWeight() / 1000.0));

            Production production = productionRepo.findByToken(token).orElseThrow(
                    () -> new ResourceNotFoundException("Production", "production token", token.toString()));

            Transactions farmerTransaction = updateTransactions(company.getName(), production.getFarmer().getEmail(),
                    Double.valueOf(storageBooking.getItemPrice() * quantity) * 0.95,
                    storageBooking.getCropName().toString(),
                    "Crop Purchase",
                    TransactionType.CREDIT, production.getFarmer());

            Transactions companyTransaction = updateTransactions(company.getName(), production.getFarmer().getEmail(),
                    Double.valueOf(storageBooking.getItemPrice() * quantity), storageBooking.getCropName().toString(),
                    "Crop Purchase",
                    TransactionType.DEBIT, user);

            Transactions warehouseTransaction = updateTransactions(
                    company.getName(), storageBooking.getStorage().getWarehouse().getName(),
                    Double.valueOf(storageBooking.getItemPrice() * quantity) * 0.05,
                    storageBooking.getCropName().toString(),
                    "Crop Purchase",
                    TransactionType.CREDIT, storageBooking.getStorage().getWarehouse().getOwner());

            user.getTransactions().add(companyTransaction);
            production.getFarmer().getTransactions().add(farmerTransaction);
            storageBooking.getStorage().getWarehouse().getOwner().getTransactions().add(warehouseTransaction);

            companyPurchases.add(purchase);

            userRepo.save(user);
            productionRepo.save(production);
            storageRepo.save(storageBooking.getStorage());
            wareHouseRepo.save(warehouse);
            storageBookingsRepo.save(storageBooking);

        });

        companyRepo.save(company);

    }

    private Transactions updateTransactions(String buyer, String seller, Double amount, String item,
            String type,
            TransactionType transactionType, User user) {
        System.out.println(amount);
        Transactions transactions = new Transactions();
        transactions.setAmount(amount);
        transactions.setBuyer(buyer);
        transactions.setSeller(seller);
        transactions.setItem(item);
        transactions.setDate(
                Date.valueOf(LocalDate.now()));
        transactions.setType(type);
        transactions.setTransactionType(transactionType);
        transactions.setUser(user);

        return transactions;
    }

    @Override
    public Map<Crops, List<AvailableCropWarehouseCard>> getWarehousesByAvailableCrops() {
        List<Warehouse> warehouses = wareHouseRepo.findAll();
        Map<Crops, List<AvailableCropWarehouseCard>> cropMap = new HashMap<>();

        warehouses.forEach(warehouse -> {
            warehouse.getStorages().forEach(storage -> {
                storage.getStorageBookings().forEach(storageBookings -> {
                    if (storageBookings.getAvailableQuantity() > 0) {
                        Crops crop = storageBookings.getCropName();
                        AvailableCropWarehouseCard card = new AvailableCropWarehouseCard(warehouse.getId(),
                                warehouse.getName(), warehouse.getAddress(), warehouse.getWarehouseImage(), crop,
                                warehouse.getOwner().getPhone(),
                                storageBookings.getItemPrice());

                        List<AvailableCropWarehouseCard> cards = cropMap.computeIfAbsent(crop, k -> new ArrayList<>());

                        cards.add(card);

                    }
                });
            });
        });

        return cropMap;
    }

    @Override
    public List<CompanyPurchasesDto> getCompanyPurchases(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        Company company = companyRepo.findByManager(user).orElseThrow(() -> {
            throw new ResourceNotFoundException("Company", "manager", user.getEmail());
        });

        return company.getCompanyPurchases().stream().map(purchase -> {
            CompanyPurchasesDto dto = modelMapper.map(purchase, CompanyPurchasesDto.class);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<Crops, int[]> getCompanyCropCards(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        Company company = companyRepo.findByManager(user).orElseThrow(() -> {
            throw new ResourceNotFoundException("Company", "manager", user.getEmail());
        });

        Map<Crops, int[]> cropMap = new HashMap<>();

        company.getCompanyPurchases().forEach(purchase -> {
            Crops crop = purchase.getCrop();
            int[] values = cropMap.computeIfAbsent(crop, k -> new int[2]);

            values[0] += purchase.getPurchaseQuantity();
            values[1] += purchase.getPurchaseTotal();
        });

        return cropMap;
    }

    @Override
    public CompanyDto getCompany(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        Company company = companyRepo.findByManager(user).orElseThrow(() -> {
            throw new ResourceNotFoundException("Company", "manager", user.getEmail());
        });

        return modelMapper.map(company, CompanyDto.class);
    }

    @Override
    public CompanyDto updateCompany(CompanyRegisterDto companyRegisterDto, String email) throws IOException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("User", "email", email);
        });

        Company company = companyRepo.findByManager(user).orElseThrow(() -> {
            throw new ResourceNotFoundException("Company", "manager", user.getEmail());
        });

        company.setAddress(companyRegisterDto.getAddress());
        company.setCompanyDetails(companyRegisterDto.getCompanyDetails());
        company.setCompanyIndustry(companyRegisterDto.getCompanyIndustry());
        company.setName(companyRegisterDto.getName());
        company.setOwnership(companyRegisterDto.getOwnership());
        company.setPin(companyRegisterDto.getPin());

        if (companyRegisterDto.getCompanyImage() == null) {
            Boolean deleted = s3Service.deleteFile(company.getCompanyImage());
        }

        if (companyRegisterDto.getCompanyImage() != null) {
            String companyImage = s3Service.uploadFile(companyRegisterDto.getCompanyImage());
            company.setCompanyImage(companyImage);
        }

        Company savedCompany = companyRepo.save(company);

        return modelMapper.map(savedCompany, CompanyDto.class);
    }

}
