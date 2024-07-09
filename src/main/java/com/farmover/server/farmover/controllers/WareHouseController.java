package com.farmover.server.farmover.controllers;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.payloads.request.WarehouseRequestDto;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.impl.S3ServiceImpl;
import com.farmover.server.farmover.services.impl.WareHouseServiceImpl;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;



@RestController
@CrossOrigin
@RequestMapping("/warehouse")
public class WareHouseController {
    @Autowired
    WareHouseServiceImpl wareHouseServiceImpl;
    @Autowired
    private UserRepo userRepo;
    @Autowired
     S3ServiceImpl s3ServiceImpl;

    @PostMapping("/addWareHouse")
    public ResponseEntity<String> addWareHouse(@RequestParam Integer ownerId ,@ModelAttribute WarehouseRequestDto requestDto) throws IOException {
        wareHouseServiceImpl.addWareHouse(ownerId, requestDto);
        return new ResponseEntity<String>("",HttpStatus.OK);
    }
    @GetMapping("/getWareHouse")
    public WareHouseDto getWareHouse(@RequestParam Integer id) {
        WareHouseDto warehouse = wareHouseServiceImpl.getWarehouse(id);
        return warehouse;
    }
    @SuppressWarnings("deprecation")
    @GetMapping("/getAllWareHouseByOwnerId")
    public  List<WareHouseDto> getAllWareHouseByOwnerId(@RequestParam Integer ownerId) {
        return wareHouseServiceImpl.getWarehouseByOwner(userRepo.getById(ownerId));
    }
    @PostMapping("/updateWareHouse")
    public ResponseEntity<String> updateWareHouse(@RequestParam Integer id,@RequestBody Warehouse ware) {
        
        wareHouseServiceImpl.updateWareHouse(ware,id);
        return new ResponseEntity<String>(ware.getName(),HttpStatus.OK); 
    }

    @PostMapping("/deleteWarehouse")
    public  ResponseEntity<String> deleteWarehouse(@RequestParam Integer id) {
        wareHouseServiceImpl.deleteWarehouse(id);
        return new ResponseEntity<String>("Deleted",HttpStatus.OK); 
    }
    
    
    
    
    
}
