package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.SuitableFor;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.StorageDto;
import com.farmover.server.farmover.payloads.request.StorageRequestDto;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.StorageService;

import io.jsonwebtoken.lang.Arrays;

@Service
public class StorageServiceImpl implements StorageService {
    @Autowired
    StorageRepo storageRepo;
    @Autowired
    WareHouseRepo wareHouseRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    S3ServiceImpl s3ServiceImpl;
    @Autowired
    SuitableForServiceImp sForServiceImp;

    @Override
    public StorageDto geStorage(Integer id) {
        Storage storage = storageRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Storage", "Storage id", id);
        });

        StorageDto dto = modelMapper.map(storage, StorageDto.class);
        dto.setSuitableFor(sForServiceImp.getAllByStorage(id));
        return dto;
    }

    @Override
    public void addStorage(Integer wareHouseId, StorageRequestDto requestDto) {
        Warehouse ware = wareHouseRepo.findById(wareHouseId)
                .orElseThrow(() -> new RuntimeException("WareHouse not found"));
        Storage storage = modelMapper.map(requestDto, Storage.class);
        List<String> name = Arrays.asList(requestDto.getSuitableFor().split(","));

        storage.setWarehouse(ware);

        Storage stoid = storageRepo.save(storage);
        for (String str : name) {
            sForServiceImp.addToStorage(stoid.getId(), str);
        }
    }

    @Override
    public void updateStorage(Integer id, Storage storage) {
        Storage str = storageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Storage not found"));
        if (storage.getAreaNumber() != null)
            str.setAreaNumber(storage.getAreaNumber());
        if (storage.getCapacity() != null)
            str.setCapacity(storage.getCapacity());
        if (storage.getPricePerKg() != null)
            str.setPricePerKg(storage.getPricePerKg());
        if (storage.getStorageType() != null)
            str.setStorageType(storage.getStorageType());
        if (storage.getTemperature() != null)
            str.setTemperature(storage.getTemperature());
        storageRepo.save(str);

    }

    @Override
    public ArrayList<StorageDto> getAllStorageByWarehouse(Warehouse warehouse) {
        ArrayList<Storage> storages = storageRepo.findByWarehouse(warehouse);
        ArrayList<StorageDto> storageDtos = new ArrayList<StorageDto>();
        for (Storage storage : storages) {
            StorageDto dto = modelMapper.map(storage, StorageDto.class);
            dto.setSuitableFor(sForServiceImp.getAllByStorage(storage.getId()));
            storageDtos.add(dto);
        }
        return storageDtos;
    }

    public void deleteStorage(Integer id) {
        storageRepo.deleteById(id);
    }

}
