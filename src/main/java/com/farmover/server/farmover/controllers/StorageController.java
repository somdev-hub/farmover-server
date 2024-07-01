package com.farmover.server.farmover.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.impl.StorageServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    StorageServiceImpl serviceImpl;
    @Autowired
    WareHouseRepo wareHouseRepo;


    @PostMapping("/addStorage")
    public ResponseEntity<String> addStorage(@RequestParam Integer wareId,@RequestBody Storage storage) {
        serviceImpl.addStorage(wareId,storage);
        return new ResponseEntity<String>(storage.getId()+"",HttpStatus.OK);
    }

    @GetMapping("/getStorage")
    public Storage getStorage(@RequestParam Integer id) {
        Storage storage = serviceImpl.geStorage(id);
        return storage;
    }

    @SuppressWarnings("deprecation")
    @GetMapping("/getAllStorageByWarehouse")
    public  ArrayList<Storage> getAllStorageByWarehouse(@RequestParam Integer wareHouseid) {
        return serviceImpl.getAllStorageByWarehouse(wareHouseRepo.getById(wareHouseid));
    }

    @PostMapping("/updateStorage")
    public ResponseEntity<String> updateStorage(@RequestParam Integer id,@RequestBody Storage storage) {
        
        serviceImpl.updateStorage(id,storage);
        return new ResponseEntity<String>(storage.getId()+"",HttpStatus.OK); 
    }

    @PostMapping("/deleteStorage")
    public  ResponseEntity<String> deleteWarehouse(@RequestParam Integer id) {
        serviceImpl.deleteStorage(id);
        return new ResponseEntity<String>("Deleted",HttpStatus.OK); 
    }
}
