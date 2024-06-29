package com.farmover.server.farmover.services;

import java.util.List;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;

public interface WareHouseService {

    Warehouse getWarehouse(Integer id);
    void addWareHouse(Integer userId,Warehouse warehouse);
    void updateWareHouse(Warehouse wh,Integer id);
    List<Warehouse> getWarehouseByOwner(User user);

}