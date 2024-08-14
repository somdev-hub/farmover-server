package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ContractDetails;
import com.farmover.server.farmover.entities.CropActivity;
import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.PaymentSessionId;
import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.ServiceStatus;
import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.StorageBookings;
import com.farmover.server.farmover.entities.TransactionType;
import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.payloads.records.OrderOverview;
import com.farmover.server.farmover.payloads.records.ProductionSalesDataRecord;
import com.farmover.server.farmover.payloads.records.ProductionServicesUsageRecord;
import com.farmover.server.farmover.payloads.records.ProductionWarehouseRecord;
import com.farmover.server.farmover.payloads.request.AddProductionToWarehouseDto;
import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;
import com.farmover.server.farmover.payloads.request.AddServicesToProductionDto;
import com.farmover.server.farmover.repositories.ContractDetailsRepo;
import com.farmover.server.farmover.repositories.CropActivityRepo;
import com.farmover.server.farmover.repositories.PaymentSessionIdRepo;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.ServicesRepo;
import com.farmover.server.farmover.repositories.StorageBookingsRepo;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.ProductionService;

import jakarta.transaction.Transactional;

@Service
public class ProductionServiceImpl implements ProductionService {

        @Autowired
        private ProductionRepo productionRepo;

        @Autowired
        private UserRepo userRepo;

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private CropActivityRepo cropActivityRepo;

        @Autowired
        private ServicesRepo servicesRepo;

        @Autowired
        private WareHouseRepo wareHouseRepo;

        @Autowired
        private StorageRepo storageRepo;

        @Autowired
        private ContractDetailsRepo contractDetailsRepo;

        @Autowired
        private StorageBookingsRepo storageBookingsRepo;

        @Autowired
        private PaymentSessionIdRepo paymentSessionIdRepo;

        @Override
        public ProductionDto addProduction(ProductionDto productionDto, String email) {
                Production production = modelMapper.map(productionDto, Production.class);

                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email ", email));

                production.setFarmer(user);
                production.setDate(
                                LocalDate.now());

                production.setCropActivities(new ArrayList<>());

                productionRepo.save(production);

                CropActivity cropActivity = new CropActivity();

                cropActivity.setActivityTitle(productionDto.getStatus());
                cropActivity.setStartDate(LocalDate.now());
                cropActivity.setActivityNumber(1);
                cropActivity.setProduction(production);

                production.getCropActivities().add(cropActivity);

                cropActivityRepo.save(cropActivity);

                return modelMapper.map(production, ProductionDto.class);
        }

        @Override
        public ProductionDto getProduction(Integer token, String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

                Production production = productionRepo.findByToken(token)
                                .orElseThrow(() -> new ResourceNotFoundException("Production", "token",
                                                Integer.toString(token)));

                if (!production.getFarmer().equals(user)) {
                        throw new ResourceNotFoundException("Production", "token", Integer.toString(token));
                }

                return modelMapper.map(production, ProductionDto.class);
        }

        @Override
        public ProductionDto updateProduction(ProductionDto productionDto, Integer token) {
                Production production = productionRepo.findByToken(token)
                                .orElseThrow(() -> new ResourceNotFoundException("Production", "token",
                                                Integer.toString(token)));

                production.setQuantity(productionDto.getQuantity());
                production.setStatus(productionDto.getStatus());

                // Step 1: Retrieve the crop activity with the highest activityNumber
                CropActivity latestCropActivity = production.getCropActivities().stream()
                                .max(Comparator.comparing(CropActivity::getActivityNumber))
                                .orElse(null);

                // Step 2: Set the end date of the crop activity with the highest activityNumber
                // to today if it exists
                if (latestCropActivity != null) {
                        latestCropActivity.setEndDate(LocalDate.now());
                        cropActivityRepo.save(latestCropActivity); // Save the updated crop activity
                }

                // Continue with adding the new crop activity
                CropActivity cropActivity = new CropActivity();
                cropActivity.setActivityTitle(productionDto.getStatus());
                cropActivity.setStartDate(LocalDate.now());
                // Assuming activityNumber needs to be set here, increment from the highest
                // number
                cropActivity.setActivityNumber(
                                latestCropActivity != null ? latestCropActivity.getActivityNumber() + 1 : 1);
                cropActivity.setProduction(production);

                production.getCropActivities().add(cropActivity);

                cropActivityRepo.save(cropActivity);

                productionRepo.save(production); // Save the production after adding the new crop activity

                return modelMapper.map(production, ProductionDto.class);
        }

        @Override
        public void deleteProduction(Integer token) {
                Production production = productionRepo.findByToken(token)
                                .orElseThrow(() -> new ResourceNotFoundException("Production", "token",
                                                Integer.toString(token)));

                productionRepo.delete(production);
        }

        @Override
        public List<ProductionDto> getProductionByFarmer(String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

                List<Production> productions = productionRepo.findByFarmer(user).orElseThrow(
                                () -> new ResourceNotFoundException("Production", "farmer email", email));

                List<ProductionDto> productionDtos = productions.stream()
                                .map(production -> modelMapper.map(production, ProductionDto.class))
                                .toList();

                return productionDtos;

        }

        @Override
        public List<CropWiseProduction> getCropWiseProduction(String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

                List<Production> productions = productionRepo.findByFarmer(user)
                                .orElseThrow(() -> new ResourceNotFoundException("Production", "farmer email", email));

                Map<Crops, Long> cropProductionMap = new HashMap<>();

                for (Production production : productions) {
                        cropProductionMap.put(production.getCrop(),
                                        cropProductionMap.getOrDefault(production.getCrop(), 0L)
                                                        + production.getQuantity());
                }

                List<CropWiseProduction> cropWiseProductions = cropProductionMap.entrySet().stream()
                                .map(entry -> new CropWiseProduction(entry.getKey(), entry.getValue()))
                                .toList();

                return cropWiseProductions;
        }

        @Transactional
        public void addServicesToProduction(AddServicesToProductionDto dto) {
                PaymentSessionId paymentId = paymentSessionIdRepo.findBySessionId(dto.getSessionId())
                                .orElseGet(() -> {
                                        PaymentSessionId newPaymentId = new PaymentSessionId();
                                        newPaymentId.setSessionId(dto.getSessionId());
                                        newPaymentId.setUsed(false);
                                        newPaymentId.setStatus("PAID");
                                        paymentSessionIdRepo.save(newPaymentId);
                                        return newPaymentId;
                                });

                if (paymentId.getUsed()) {
                        throw new RuntimeException("Session Id already used");
                }

                paymentId.setUsed(true); // Set used to true after the transaction
                paymentId.setUsedDate(LocalDate.now());
                paymentSessionIdRepo.save(paymentId);

                // Use a stream to iterate over serviceIds and durations
                IntStream.range(0, dto.getServiceIds().size()).forEach(i -> {
                        AddServiceToProductionDto addServiceToProduction = new AddServiceToProductionDto();
                        addServiceToProduction.setServiceId(dto.getServiceIds().get(i));
                        addServiceToProduction.setDuration(dto.getDurations().get(i));
                        addServiceToProduction.setEmail(dto.getEmail());
                        addServiceToProduction.setProductionToken(dto.getProductionToken());

                        addServiceToProductionHandler(addServiceToProduction);
                });
        }

        @Override
        @Transactional
        public void addServiceToProduction(AddServiceToProductionDto dto) {

                PaymentSessionId paymentId = paymentSessionIdRepo.findBySessionId(dto.getSessionId())
                                .orElseGet(() -> {
                                        PaymentSessionId newPaymentId = new PaymentSessionId();
                                        newPaymentId.setSessionId(dto.getSessionId());
                                        newPaymentId.setUsed(false);
                                        newPaymentId.setStatus("PAID");
                                        paymentSessionIdRepo.save(newPaymentId);
                                        return newPaymentId;
                                });

                if (paymentId.getUsed()) {
                        throw new RuntimeException("Session Id already used");
                }

                paymentId.setUsed(true); // Set used to true after the transaction
                paymentId.setUsedDate(LocalDate.now());

                paymentSessionIdRepo.save(paymentId);

                addServiceToProductionHandler(dto);

        }

        private void addServiceToProductionHandler(AddServiceToProductionDto dto) {
                Services service = servicesRepo.findById(dto.getServiceId())
                                .orElseThrow(() -> new ResourceNotFoundException("Service", "serviceId",
                                                dto.getServiceId().toString()));
                Production production = productionRepo.findByToken(dto.getProductionToken())
                                .orElseThrow(() -> new ResourceNotFoundException("Production", "token",
                                                dto.getProductionToken().toString()));
                User user = userRepo.findByEmail(dto.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", dto.getEmail()));
                User serviceOwner = service.getOwner();

                Date transactionDate = new Date(System.currentTimeMillis());
                double amount = service.getPricePerDay() * dto.getDuration();

                Transactions buyer_Transaction = createTransaction(service, user, dto.getEmail(), amount,
                                transactionDate,
                                TransactionType.DEBIT);
                Transactions seller_Transaction = createTransaction(service, serviceOwner, serviceOwner.getEmail(),
                                amount,
                                transactionDate, TransactionType.CREDIT);

                List<Transactions> userTransactions = user.getTransactions();
                List<Transactions> serviceOwnerTransactions = serviceOwner.getTransactions();

                userTransactions.add(buyer_Transaction);
                serviceOwnerTransactions.add(seller_Transaction);

                user.setTransactions(userTransactions);

                serviceOwner.setTransactions(serviceOwnerTransactions);

                userRepo.saveAll(Arrays.asList(user, serviceOwner));

                service.setLastOperated(LocalDate.now());

                ContractDetails contractDetail = new ContractDetails();
                contractDetail.setDuration(dto.getDuration());
                contractDetail.setContractSignDate(LocalDate.now());
                contractDetail.setService(service);
                contractDetail.setFarmer(user.getEmail());
                contractDetail.setPrice(amount);
                contractDetail.setAddress(user.getAddress());
                contractDetail.setPhone(user.getPhone());
                contractDetail.setStatus("COMMISSIONED");
                contractDetail.setProductionToken(dto.getProductionToken());

                service.getContractDetails().add(contractDetail);
                service.setStatus(ServiceStatus.COMMISSIONED);

                if (!production.getServices().contains(service)) {
                        production.getServices().add(service);
                        service.getProductions().add(production);
                }

                servicesRepo.save(service);
                productionRepo.save(production);
        }

        private Transactions createTransaction(Services service, User user, String email, double amount,
                        Date transactionDate,
                        TransactionType transactionType) {
                Transactions transaction = new Transactions();
                transaction.setAmount(amount);
                transaction.setBuyer(email);
                transaction.setSeller(service.getOwner().getEmail());
                transaction.setItem(service.getServiceType());
                transaction.setDate(transactionDate);
                transaction.setType("service");
                transaction.setTransactionType(transactionType);
                transaction.setUser(user);
                return transaction;
        }

        @Override
        @Transactional
        public void addProductionToWarehouse(AddProductionToWarehouseDto dto, String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
                Production production = productionRepo.findByToken(dto.getProductionToken()).orElseThrow(
                                () -> new ResourceNotFoundException("Production", "token",
                                                dto.getProductionToken().toString()));
                Warehouse warehouse = wareHouseRepo.findById(dto.getWarehouseId())
                                .orElseThrow(() -> new ResourceNotFoundException("WareHouse", "wareHouse id",
                                                dto.getWarehouseId().toString()));

                Storage storage = warehouse.getStorages().stream()
                                .filter(s -> s.getStorageType().equals(dto.getStorageType()))
                                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Storage", "storage type",
                                                dto.getStorageType().toString()));

                if (storage.getAvailableCapacity() < (dto.getWeight() / 1000.0)) {
                        throw new RuntimeException("Not enough capacity");
                }

                StorageBookings booking = createStorageBooking(dto, email, dto.getProductionToken(), storage);

                // Update storage capacity directly
                storage.setAvailableCapacity(storage.getAvailableCapacity() - (dto.getWeight() / 1000.0));
                storage.getStorageBookings().add(booking);

                // Create and add transactions directly
                Transactions userTransaction = createTransaction(booking, email, warehouse.getOwner().getEmail(), user,
                                TransactionType.DEBIT);
                Transactions warehouseTransaction = createTransaction(booking, email, warehouse.getName(),
                                warehouse.getOwner(), TransactionType.CREDIT);

                user.getTransactions().add(userTransaction);
                warehouse.getOwner().getTransactions().add(warehouseTransaction);

                production.setStatus("stored");

                // Update the end date of the latest CropActivity
                production.getCropActivities().stream()
                                .max(Comparator.comparing(CropActivity::getActivityNumber))
                                .ifPresent(latestCropActivity -> {
                                        latestCropActivity.setEndDate(LocalDate.now());
                                        cropActivityRepo.save(latestCropActivity);
                                });

                // Create and add a new CropActivity for "Stored"
                CropActivity newCropActivity = new CropActivity();
                newCropActivity.setActivityTitle("Stored");
                newCropActivity.setStartDate(LocalDate.now());
                newCropActivity.setEndDate(LocalDate.now());
                newCropActivity.setActivityNumber(production.getCropActivities().size() + 1);
                newCropActivity.setProduction(production);

                production.getCropActivities().add(newCropActivity);

                // Batch save entities
                userRepo.save(user);
                userRepo.save(warehouse.getOwner());
                storageRepo.save(storage);
                production.setWarehouse(warehouse);
                productionRepo.save(production);
        }

        private StorageBookings createStorageBooking(AddProductionToWarehouseDto dto, String email, Integer token,
                        Storage storage) {
                StorageBookings booking = new StorageBookings();
                booking.setStorage(storage);
                booking.setBookedWeight(dto.getWeight());
                booking.setAvailableQuantity(dto.getWeight());
                booking.setClientEmail(email);
                booking.setBookingDuration(dto.getDuration());
                booking.setBookingDate(LocalDate.now());
                booking.setBookedPrice(dto.getWeight() * storage.getPricePerKg());
                booking.setItemPrice(dto.getMinimumPrice());
                booking.setItemUnit(dto.getMinimumUnit());
                booking.setMarkForSale(dto.getMarkForSale());
                booking.setProductionToken(token);
                booking.setCropName(productionRepo.findByToken(token).get().getCrop());
                booking.setStatus("booked");
                return booking;
        }

        private Transactions createTransaction(StorageBookings booking, String buyer, String seller,
                        User user, TransactionType type) {
                Transactions transaction = new Transactions();
                transaction.setAmount(booking.getBookedPrice());
                transaction.setBuyer(buyer);
                transaction.setSeller(seller);
                transaction.setItem("Storage Booking");
                transaction.setDate(Date.valueOf(LocalDate.now()));
                transaction.setType("storage");
                transaction.setTransactionType(type);
                transaction.setUser(user);
                return transaction;
        }

        @Override
        public List<ProductionSalesDataRecord> getSalesData(String email) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

                List<Transactions> transactions = user.getTransactions().stream()
                                .filter(transaction -> transaction.getTransactionType().equals(TransactionType.CREDIT))
                                .toList();

                List<ProductionSalesDataRecord> productionSalesDataRecords = new ArrayList<>();

                for (Transactions transaction : transactions) {
                        // if (transaction.getType() == "Crop Purchase") {

                        ProductionSalesDataRecord record = new ProductionSalesDataRecord(
                                        transaction.getBuyer(),
                                        transaction.getItem(),
                                        transaction.getDate(),
                                        transaction.getAmount());

                        productionSalesDataRecords.add(record);
                        // }
                }

                return productionSalesDataRecords;
        }

        @Override
        public Page<OrderOverview> getOrderOverview(String email, Integer page, Integer size) {
                User user = userRepo.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

                List<Transactions> transactions = user.getTransactions().stream()
                                .toList();

                Pageable pageable = PageRequest.of(page, size);

                List<OrderOverview> orderOverviews = new ArrayList<>();

                for (Transactions transaction : transactions) {

                        OrderOverview orderOverview;
                        if (transaction.getTransactionType().equals(TransactionType.CREDIT)) {

                                orderOverview = new OrderOverview(
                                                transaction.getAmount(),
                                                transaction.getBuyer(),
                                                transaction.getDate(),
                                                transaction.getTransactionType().toString()

                                );
                        } else {

                                orderOverview = new OrderOverview(
                                                transaction.getAmount(),
                                                transaction.getSeller(),
                                                transaction.getDate(),
                                                transaction.getTransactionType().toString()

                                );
                        }

                        orderOverviews.add(orderOverview);
                }

                Collections.sort(orderOverviews, new Comparator<OrderOverview>() {
                        @Override
                        public int compare(OrderOverview o1, OrderOverview o2) {
                                return o2.date().compareTo(o1.date());
                        }
                });

                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), orderOverviews.size());
                List<OrderOverview> pagedOrderOverviews = orderOverviews.subList(start, end);

                return new PageImpl<>(pagedOrderOverviews, pageable, orderOverviews.size());
        }

        @Override
        public List<ProductionServicesUsageRecord> getServiceUsage(String email) {

                List<ProductionServicesUsageRecord> productionServicesUsageRecords = new ArrayList<>();

                List<ContractDetails> contractDetails = contractDetailsRepo.findByFarmer(email)
                                .orElseThrow(() -> new ResourceNotFoundException("ContractDetails", "farmer email",
                                                email));

                contractDetails.forEach(detail -> {
                        Production production = productionRepo.findByToken(detail.getProductionToken())
                                        .orElseThrow(() -> new ResourceNotFoundException("Production", "token",
                                                        detail.getProductionToken().toString()));
                        ProductionServicesUsageRecord record = new ProductionServicesUsageRecord(
                                        detail.getService().getServiceName(), production.getCrop(),
                                        detail.getContractSignDate(), detail.getDuration(), detail.getPrice(),
                                        detail.getProductionToken(), detail.getStatus());

                        productionServicesUsageRecords.add(record);
                });

                return productionServicesUsageRecords;
        }

        @Override
        public List<ProductionWarehouseRecord> getUsedWarehouses(String email) {
                List<StorageBookings> bookings = storageBookingsRepo.findByClientEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Bookings", "email", email));

                List<ProductionWarehouseRecord> productionWarehouseRecords = new ArrayList<>();

                bookings.forEach(booking -> {
                        ProductionWarehouseRecord record = new ProductionWarehouseRecord(
                                        booking.getStorage().getWarehouse().getName(),
                                        booking.getCropName(),
                                        booking.getProductionToken(),
                                        booking.getBookedWeight(),
                                        booking.getItemUnit(),
                                        booking.getBookingDate(),
                                        booking.getBookedPrice(),
                                        booking.getBookingDuration());

                        productionWarehouseRecords.add(record);
                });

                // Sort the list by booking date
                productionWarehouseRecords.sort(Comparator.comparing(ProductionWarehouseRecord::date).reversed());

                return productionWarehouseRecords;
        }

}
