package com.farmover.server.farmover.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.services.impl.WareHouseServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequestMapping("/warehouse")
public class WareHouseController {
    @Autowired
    WareHouseServiceImpl wareHouseServiceImpl;

    @PostMapping("/addWareHouse")
    public ResponseEntity<String> addWareHouse(@RequestParam Integer ownerId,@RequestBody Warehouse warehouse) {
        wareHouseServiceImpl.addWareHouse(ownerId,warehouse);
        return new ResponseEntity<String>(warehouse.getName(),HttpStatus.OK);
    }
    
}
