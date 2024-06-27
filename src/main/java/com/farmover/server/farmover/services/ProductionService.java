package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.ProductionDto;

public interface ProductionService {

    ProductionDto addProduction(ProductionDto productionDto, String email);

    ProductionDto getProduction(Integer token);

    ProductionDto updateProduction(ProductionDto productionDto, Integer token);

    void deleteProduction(Integer token);

    ProductionDto getProductionByStatus(String status);

    ProductionDto getProductionByWarehouse(Integer warehouseId);

    ProductionDto getProductionByFarmer(Integer farmerId);

    ProductionDto getProductionByCrop(Integer cropId);

    List<ProductionDto> getAllProductions();

}
