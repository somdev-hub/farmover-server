package com.farmover.server.farmover.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.WareHouseService;

@Service
public class WareHouseServiceImpl implements WareHouseService{
    @Autowired
    WareHouseRepo wareHouseRepo;
    @Autowired
     UserRepo userRepo;

    @Override
    public Warehouse getWarehouse(Integer id) {
       return wareHouseRepo.findById(id).orElseThrow(() -> {
        throw new ResourceNotFoundException("WareHouse", "wareHouse id", id);
    });
    }

    @Override
    public void addWareHouse(Integer userId,Warehouse warehouse) {
        User owner = userRepo.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

        warehouse.setOwner(owner);
        wareHouseRepo.save(warehouse);
    }

    @Override
    public void updateWareHouse(Warehouse wh,Integer id) {
        Warehouse warehouse = wareHouseRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("WareHouse", "wareHouse id", id);
        });
        warehouse.setAddress(wh.getAddress());
        warehouse.setName(wh.getName());
        wareHouseRepo.save(warehouse);
    }

    @Override
    public ArrayList<Warehouse> getWarehouseByOwner(User user) {
        return wareHouseRepo.findByOwner(user);
    }
    
}
