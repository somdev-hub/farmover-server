package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.payloads.WareHouseDto;

public interface WareHouseService {

    WareHouseDto getWarehouse(Integer id);
    void addWareHouse(Integer userId,Warehouse warehouse);
    void updateWareHouse(Warehouse wh,Integer id);
    List<WareHouseDto> getWarehouseByOwner(User user);
    void deleteWarehouse(Integer id);

}