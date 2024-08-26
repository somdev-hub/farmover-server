package com.farmover.server.farmover.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.StorageCardDto;
import com.farmover.server.farmover.payloads.StorageDto;
import com.farmover.server.farmover.payloads.request.StorageRequestDto;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.UserRepo;
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
    @Autowired
    S3ServiceImpl s3ServiceImpl;
    @Autowired
    SuitableForServiceImp sForServiceImp;
    @Autowired
    UserRepo userRepo;

    @Override
    public StorageDto geStorage(Integer id) {
        Storage storage = storageRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("Storage", "Storage id", id.toString());
        });

        StorageDto dto = modelMapper.map(storage, StorageDto.class);
        return dto;
    }

    @Override
    public StorageDto addStorage(String email, StorageRequestDto requestDto) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse ware = wareHouseRepo.findByOwner(user);

        Storage storage = modelMapper.map(requestDto, Storage.class);

        storage.setWarehouse(ware);

        storage.setAvailableCapacity(requestDto.getCapacity());

        storage.setStorageBookings(new ArrayList<>());

        Storage stoid = storageRepo.save(storage);

        StorageDto dto = modelMapper.map(stoid, StorageDto.class);

        return dto;

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

    public void deleteStorage(Integer id) {
        storageRepo.deleteById(id);
    }

    @Override
    public ArrayList<StorageDto> getAllStorageByOwner(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        Warehouse warehouse = wareHouseRepo.findByOwner(user);
        ArrayList<Storage> storages = storageRepo.findByWarehouse(warehouse);
        ArrayList<StorageDto> dtos = new ArrayList<>();
        for (Storage storage : storages) {
            StorageDto dto = modelMapper.map(storage, StorageDto.class);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<StorageCardDto> getStorageCards(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = wareHouseRepo.findByOwner(user);

        List<Storage> storages = storageRepo.findByWarehouse(warehouse);

        List<StorageCardDto> storageCardDtoList = storages.stream().map(storage -> {
            StorageCardDto storageCardDto = new StorageCardDto();
            storageCardDto.setAvailableCapacity(storage.getAvailableCapacity());
            storageCardDto.setCapacity(storage.getCapacity());
            storageCardDto.setStorageType(storage.getStorageType());
            storageCardDto.setId(storage.getId());

            return storageCardDto;
        }).toList();

        return storageCardDtoList;
    }

}
