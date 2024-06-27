package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.ProductionService;

@Service
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    private ProductionRepo productionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductionDto addProduction(ProductionDto productionDto, String email) {
        Production production = modelMapper.map(productionDto, Production.class);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email " + email, 0));

        production.setFarmer(user);
        production.setDate(
                Date.valueOf(LocalDate.now()));

        productionRepo.save(production);

        return modelMapper.map(production, ProductionDto.class);
    }

    @Override
    public ProductionDto getProduction(Integer token) {
        Production production = productionRepo.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Production", "token", token));

        return modelMapper.map(production, ProductionDto.class);
    }

    @Override
    public ProductionDto updateProduction(ProductionDto productionDto, Integer token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduction'");
    }

    @Override
    public void deleteProduction(Integer token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProduction'");
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
    public ProductionDto getProductionByFarmer(Integer farmerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductionByFarmer'");
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

}
