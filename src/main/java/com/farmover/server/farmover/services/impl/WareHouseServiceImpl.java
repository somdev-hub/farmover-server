package com.farmover.server.farmover.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.WareHouseService;

@Service
public class WareHouseServiceImpl implements WareHouseService {
    @Autowired
    WareHouseRepo wareHouseRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WareHouseDto getWarehouse(Integer id) {
        Warehouse warehouse =wareHouseRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("WareHouse", "wareHouse id", id);
        });
        return modelMapper.map(warehouse, WareHouseDto.class);
    }

    @Override
    public void addWareHouse(Integer userId, Warehouse warehouse) {
        User owner = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        warehouse.setOwner(owner);
        wareHouseRepo.save(warehouse);
    }

    @Override
    public void updateWareHouse(Warehouse wh, Integer id) {
        Warehouse warehouse = wareHouseRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("WareHouse", "wareHouse id", id);
        });
        if (wh.getAddress() != null)
            warehouse.setAddress(wh.getAddress());
        if (wh.getName() != null)
            warehouse.setName(wh.getName());
        wareHouseRepo.save(warehouse);
    }

    @Override
    public List<WareHouseDto> getWarehouseByOwner(User user) {
        List<Warehouse> warehouses =  wareHouseRepo.findByOwner(user);
        List<WareHouseDto> wareHouseDtos = new ArrayList<WareHouseDto>();
        for(Warehouse warehouse: warehouses){
            wareHouseDtos.add(modelMapper.map(warehouse, WareHouseDto.class));
        }
        return wareHouseDtos;
    }

    @Override
    public void deleteWarehouse(Integer id) {
        wareHouseRepo.deleteById(id);
    }

}
