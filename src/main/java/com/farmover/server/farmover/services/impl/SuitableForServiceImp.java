package com.farmover.server.farmover.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.SuitableFor;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.repositories.StorageRepo;
import com.farmover.server.farmover.repositories.SuitableForRepo;
import com.farmover.server.farmover.services.SuitableForService;

@Service
public class SuitableForServiceImp implements SuitableForService {
    @Autowired
    SuitableForRepo repo;
    @Autowired
    StorageRepo rStorageRepo;
    @Override
    public void addToStorage(int storageId, String name) {
        Storage storage = rStorageRepo.findById(storageId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Storage", "Storage id", storageId);
        });
        SuitableFor suitableFor =new SuitableFor();
        suitableFor.setName(name);
        suitableFor.setStorage(storage);
        repo.save(suitableFor);
    }

    @Override
    public List<String> getAllByStorage(int storageId) {
        Storage storage = rStorageRepo.findById(storageId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Storage", "Storage id", storageId);
        });
        
        List<SuitableFor> suitableFor = repo.findByStorage(storage);
        List<String> name = new ArrayList<>();
        for(SuitableFor suitable:suitableFor){
            name.add(suitable.getName());
        }
        return name;
    }
    
}
