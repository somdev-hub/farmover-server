package com.farmover.server.farmover.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.payloads.records.OrderOverview;
import com.farmover.server.farmover.payloads.records.ProductionSalesDataRecord;
import com.farmover.server.farmover.payloads.records.ProductionServicesUsageRecord;
import com.farmover.server.farmover.payloads.records.ProductionWarehouseRecord;
import com.farmover.server.farmover.payloads.request.AddProductionToWarehouseDto;
import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;

public interface ProductionService {

    ProductionDto addProduction(ProductionDto productionDto, String email);

    ProductionDto getProduction(Integer token, String email);

    ProductionDto updateProduction(ProductionDto productionDto, Integer token);

    void deleteProduction(Integer token);

    List<ProductionDto> getProductionByFarmer(String email);

    List<CropWiseProduction> getCropWiseProduction(String email);

    void addServiceToProduction(AddServiceToProductionDto addServiceToProductionDto);

    void addProductionToWarehouse(AddProductionToWarehouseDto dto, String email);

    List<ProductionSalesDataRecord> getSalesData(String email);

    Page<OrderOverview> getOrderOverview(String email, Integer page, Integer size);

    List<ProductionServicesUsageRecord> getServiceUsage(String email);

    List<ProductionWarehouseRecord> getUsedWarehouses(String email);

}
