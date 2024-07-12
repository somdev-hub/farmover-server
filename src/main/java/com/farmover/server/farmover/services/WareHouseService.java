package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.payloads.request.WarehouseRequestDto;

public interface WareHouseService {

    WareHouseDto getWarehouse(Integer id);
    void addWareHouse(Integer userId,WarehouseRequestDto requestDto);
    void updateWareHouse(Warehouse wh,Integer id);
    List<WareHouseDto> getWarehouseByOwner(User user);
    void deleteWarehouse(Integer id);

}