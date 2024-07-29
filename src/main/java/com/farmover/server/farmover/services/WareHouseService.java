package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;

import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.payloads.StorageBookingsDto;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.payloads.WarehouseCardDto;
import com.farmover.server.farmover.payloads.records.WarehouseSalesRecord;
import com.farmover.server.farmover.payloads.request.WarehouseRequestDto;

public interface WareHouseService {

    WareHouseDto getWarehouse(Integer id);

    List<WarehouseCardDto> getWarehouses();

    void addWareHouse(String email, WarehouseRequestDto requestDto) throws IOException;

    void updateWareHouse(Warehouse wh, Integer id);

    WareHouseDto getWarehouseByOwner(String email);

    void deleteWarehouse(Integer id);

    List<StorageBookingsDto> getBookings(String email);

    List<WarehouseSalesRecord> getSalesOverview(String email);

}