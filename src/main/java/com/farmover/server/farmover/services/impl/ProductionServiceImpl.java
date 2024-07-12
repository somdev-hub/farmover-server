package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ContractDetails;
import com.farmover.server.farmover.entities.CropActivity;
import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.TransactionType;
import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;
import com.farmover.server.farmover.repositories.CropActivityRepo;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.ServicesRepo;
import com.farmover.server.farmover.repositories.UserRepo;
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
        public ProductionDto getProductionByStatus(String status) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductionByStatus'");
        }

        @Override
        public ProductionDto getProductionByWarehouse(Integer warehouseId) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductionByWarehouse'");
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
        public ProductionDto getProductionByCrop(Integer cropId) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductionByCrop'");
        }

        @Override
        public List<ProductionDto> getAllProductions() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAllProductions'");
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

        @Override
        @Transactional
        public void addServiceToProduction(AddServiceToProductionDto dto) {
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

                service.getContractDetails().add(contractDetail);

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

}
