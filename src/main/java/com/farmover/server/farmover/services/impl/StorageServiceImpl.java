package com.farmover.server.farmover.services.impl;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.StorageDto;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.StorageService;

@Service
public class StorageServiceImpl implements StorageService {
    @Autowired
    StorageRepo storageRepo;
    @Autowired
    WareHouseRepo wareHouseRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public StorageDto geStorage(Integer id) {
        Storage storage = storageRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Storage", "Storage id", id);
        });
        return modelMapper.map(storage, StorageDto.class);
    }

    @Override
    public void addStorage(Integer wareHouseId, Storage storage) {
        Warehouse ware = wareHouseRepo.findById(wareHouseId)
        .orElseThrow(() -> new RuntimeException("WareHouse not found"));
        storage.setWarehouse(ware);
        storageRepo.save(storage);
    }

    @Override
    public void updateStorage(Integer id, Storage storage) {
        Storage str = storageRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Storage not found"));
        if(storage.getAreaNumber()!=null)str.setAreaNumber(storage.getAreaNumber());
        if(storage.getCapacity()!=null)str.setCapacity(storage.getCapacity());
        if(storage.getPricePerKg()!=null)str.setPricePerKg(storage.getPricePerKg());
        if(storage.getStorageType()!=null)str.setStorageType(storage.getStorageType());
        if(storage.getSuitableFor()!=null)str.setSuitableFor(storage.getSuitableFor());
        if(storage.getTemperature()!=null)str.setTemperature(storage.getTemperature());
        storageRepo.save(str);

    }

    @Override
    public ArrayList<StorageDto> getAllStorageByWarehouse(Warehouse warehouse) {
        ArrayList<Storage>storages=storageRepo.findByWarehouse(warehouse);
        ArrayList<StorageDto> storageDtos = new ArrayList<StorageDto>();
        for(Storage storage:storages){
            storageDtos.add(modelMapper.map(storage, StorageDto.class));
        }
        return storageDtos;
    }

    public void deleteStorage(Integer id) {
        storageRepo.deleteById(id);
    }

}
