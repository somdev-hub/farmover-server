package com.farmover.server.farmover.services.impl;

import java.io.IOException;
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
import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.StorageBookings;
import com.farmover.server.farmover.entities.StorageType;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.CompanyDto;
import com.farmover.server.farmover.payloads.CompanyWarehouseCardDto;
import com.farmover.server.farmover.payloads.FarmerItem;
import com.farmover.server.farmover.payloads.records.RegisteredFarmersInfo;
import com.farmover.server.farmover.payloads.request.CompanyRegisterDto;
import com.farmover.server.farmover.repositories.CompanyRepo;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.CompanyServices;

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
                farmerItem.setWeight(storageBookings.getBookedWeight());

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

}
