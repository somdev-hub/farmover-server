package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.StorageType;
import com.farmover.server.farmover.payloads.CompanyDto;
import com.farmover.server.farmover.payloads.CompanyWarehouseCardDto;
import com.farmover.server.farmover.payloads.FarmerItem;
import com.farmover.server.farmover.payloads.records.RegisteredFarmersInfo;
import com.farmover.server.farmover.payloads.request.CompanyRegisterDto;

public interface CompanyServices {

    CompanyDto registerCompany(CompanyRegisterDto companyRegisterDto,String email) throws IOException;

    List<CompanyWarehouseCardDto> getWarehouseCardDtos();

    Map<StorageType, Map<Crops, ArrayList<FarmerItem>>> getWarehouseMarketData(Integer warehouseId);

    List<RegisteredFarmersInfo> getRegisteredFarmers(Integer warehouseId);
}
