package com.farmover.server.farmover.controllers;

import java.util.ArrayList;
import java.util.List;

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
import com.farmover.server.farmover.payloads.StorageCardDto;
import com.farmover.server.farmover.payloads.StorageDto;
import com.farmover.server.farmover.payloads.request.StorageRequestDto;
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
    public ResponseEntity<StorageDto> addStorage(@RequestParam String email,
            @RequestBody StorageRequestDto storage) {
        StorageDto storageDto = serviceImpl.addStorage(email, storage);
        return new ResponseEntity<StorageDto>(storageDto, HttpStatus.CREATED);
    }

    @GetMapping("/getStorage")
    public StorageDto getStorage(@RequestParam Integer id) {

        return serviceImpl.geStorage(id);
    }

    @GetMapping("/")
    public ResponseEntity<ArrayList<StorageDto>> getAllStorageByOwner(@RequestParam String email) {
        ArrayList<StorageDto> storage = serviceImpl.getAllStorageByOwner(email);
        return new ResponseEntity<ArrayList<StorageDto>>(storage, HttpStatus.OK);
    }

    @PostMapping("/updateStorage")
    public ResponseEntity<String> updateStorage(@RequestParam Integer id, @RequestBody Storage storage) {

        serviceImpl.updateStorage(id, storage);
        return new ResponseEntity<String>(storage.getId() + "", HttpStatus.OK);
    }

    @PostMapping("/deleteStorage")
    public ResponseEntity<String> deleteWarehouse(@RequestParam Integer id) {
        serviceImpl.deleteStorage(id);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/card")
    public ResponseEntity<List<StorageCardDto>> getStorageCards(@RequestParam String email) {
        List<StorageCardDto> storageCards = serviceImpl.getStorageCards(email);
        return new ResponseEntity<List<StorageCardDto>>(storageCards, HttpStatus.OK);
    }

}
