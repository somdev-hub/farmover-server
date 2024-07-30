package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.payloads.records.OrderOverview;
import com.farmover.server.farmover.payloads.records.ProductionSalesDataRecord;
import com.farmover.server.farmover.payloads.request.AddProductionToWarehouseDto;
import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;

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

    void addServiceToProduction(AddServiceToProductionDto addServiceToProductionDto);

    void addProductionToWarehouse(AddProductionToWarehouseDto dto, String email);

    List<ProductionSalesDataRecord> getSalesData(String email);

    List<OrderOverview> getOrderOverview(String email);

}
