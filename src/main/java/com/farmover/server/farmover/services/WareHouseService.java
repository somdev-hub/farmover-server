package com.farmover.server.farmover.services;

import java.util.ArrayList;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;

public interface WareHouseService {

    Warehouse getWarehouse(Integer id);
    void addWareHouse(Integer userId,Warehouse warehouse);
    void updateWareHouse(Warehouse wh,Integer id);
    ArrayList<Warehouse> getWarehouseByOwner(User user);
    void deleteWarehouse(Integer id);

}