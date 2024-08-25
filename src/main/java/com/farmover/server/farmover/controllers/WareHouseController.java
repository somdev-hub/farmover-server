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
import com.farmover.server.farmover.payloads.StorageBookingsDto;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.payloads.WarehouseCardDto;
import com.farmover.server.farmover.payloads.records.MonthStorageUsageOverviewRecord;
import com.farmover.server.farmover.payloads.records.WarehouseMonthlySalesRecord;
import com.farmover.server.farmover.payloads.records.WarehouseSalesRecord;
import com.farmover.server.farmover.payloads.request.WarehouseRequestDto;
import com.farmover.server.farmover.services.impl.S3ServiceImpl;
import com.farmover.server.farmover.services.impl.WareHouseServiceImpl;
import org.springframework.web.bind.annotation.PutMapping;

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

    @GetMapping("/overview")
    public ResponseEntity<List<WarehouseSalesRecord>> getWarehouseSales(@RequestParam String email) {
        return new ResponseEntity<List<WarehouseSalesRecord>>(wareHouseServiceImpl.getSalesOverview(email),
                HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MonthStorageUsageOverviewRecord>> getRecents(@RequestParam String email) {
        return new ResponseEntity<List<MonthStorageUsageOverviewRecord>>(
                wareHouseServiceImpl.getMonthStorageUsageOverview(email),
                HttpStatus.OK);
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<List<WarehouseMonthlySalesRecord>> getSales(@RequestParam String email) {
        return new ResponseEntity<List<WarehouseMonthlySalesRecord>>(wareHouseServiceImpl.getSalesRecord(email),
                HttpStatus.OK);
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> uodateWarehouse(@PathVariable String email, @ModelAttribute WarehouseRequestDto dto)
            throws IOException {
        wareHouseServiceImpl.updateWarehouse(email, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
