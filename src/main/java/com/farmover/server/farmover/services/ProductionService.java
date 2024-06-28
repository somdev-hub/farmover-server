package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;

public interface ProductionService {

    ProductionDto addProduction(ProductionDto productionDto, String email);

    ProductionDto getProduction(Integer token, String email);

    ProductionDto updateProduction(ProductionDto productionDto, Integer token);

    void deleteProduction(Integer token);

    ProductionDto getProductionByStatus(String status);

    ProductionDto getProductionByWarehouse(Integer warehouseId);

    List<ProductionDto> getProductionByFarmer(String email);

    ProductionDto getProductionByCrop(Integer cropId);

    List<CropWiseProduction> getCropWiseProduction(String email);

    List<ProductionDto> getAllProductions();

}
