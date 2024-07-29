package com.farmover.server.farmover.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.payloads.CompanyWarehouseCardDto;
import com.farmover.server.farmover.payloads.StorageBookingsDto;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.payloads.WarehouseCardDto;
import com.farmover.server.farmover.payloads.request.WarehouseRequestDto;
import com.farmover.server.farmover.services.impl.S3ServiceImpl;
import com.farmover.server.farmover.services.impl.WareHouseServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/warehouse")
public class WareHouseController {

    @Autowired
    WareHouseServiceImpl wareHouseServiceImpl;

    @Autowired
    S3ServiceImpl s3ServiceImpl;

    @PostMapping("/addWarehouse")
    public ResponseEntity<String> addWareHouse(@RequestParam String email,
            @ModelAttribute WarehouseRequestDto requestDto) throws IOException {
        wareHouseServiceImpl.addWareHouse(email, requestDto);
        return new ResponseEntity<String>("", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public WareHouseDto getWareHouse(@PathVariable Integer id) {
        WareHouseDto warehouse = wareHouseServiceImpl.getWarehouse(id);
        return warehouse;
    }

    @GetMapping("/")
    public ResponseEntity<WareHouseDto> getWarehouseByOwner(@RequestParam String email) {
        WareHouseDto warehouse = wareHouseServiceImpl.getWarehouseByOwner(email);
        return new ResponseEntity<WareHouseDto>(warehouse, HttpStatus.OK);
    }

    @PostMapping("/updateWareHouse")
    public ResponseEntity<String> updateWareHouse(@RequestParam Integer id, @RequestBody Warehouse ware) {

        wareHouseServiceImpl.updateWareHouse(ware, id);
        return new ResponseEntity<String>(ware.getName(), HttpStatus.OK);
    }

    @PostMapping("/deleteWarehouse")
    public ResponseEntity<String> deleteWarehouse(@RequestParam Integer id) {
        wareHouseServiceImpl.deleteWarehouse(id);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WarehouseCardDto>> getWarehouses() {
        return new ResponseEntity<List<WarehouseCardDto>>(wareHouseServiceImpl.getWarehouses(), HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<StorageBookingsDto>> getBookings(@RequestParam String email) {
        return new ResponseEntity<List<StorageBookingsDto>>(wareHouseServiceImpl.getBookings(email), HttpStatus.OK);
    }

}
