package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.entities.WarehouseFacilities;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.StorageBookingsDto;
import com.farmover.server.farmover.payloads.WareHouseDto;
import com.farmover.server.farmover.payloads.WarehouseCardDto;
import com.farmover.server.farmover.payloads.request.WarehouseRequestDto;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.farmover.server.farmover.services.WareHouseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WareHouseServiceImpl implements WareHouseService {
    @Autowired
    WareHouseRepo wareHouseRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    S3ServiceImpl s3ServiceImpl;
    @Autowired
    ProductionRepo productionRepo;

    @Override
    public WareHouseDto getWarehouse(Integer id) {
        Warehouse warehouse = wareHouseRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("WareHouse", "wareHouse id", id.toString());
        });

        WareHouseDto warehouseDto = modelMapper.map(warehouse, WareHouseDto.class);
        warehouseDto.setFacilityList(warehouse.getFacilities().stream().map(WarehouseFacilities::getFacility)
                .collect(Collectors.toList()));
        return warehouseDto;
    }

    @Override
    public void addWareHouse(String email, WarehouseRequestDto requestDto) throws IOException {
        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = modelMapper.map(requestDto, Warehouse.class);
        warehouse.setOwner(owner);

        List<String> facilityStrings = objectMapper.readValue(requestDto.getFacilities(),
                new TypeReference<List<String>>() {
                });

        List<WarehouseFacilities> warehouseFacilities = new ArrayList<>();

        for (String facility : facilityStrings) {
            WarehouseFacilities warehouseFacility = new WarehouseFacilities();
            warehouseFacility.setFacility(facility);
            warehouseFacility.setWarehouse(warehouse);
            warehouseFacilities.add(warehouseFacility);
        }

        warehouse.setFacilities(warehouseFacilities);

        String bg = "";

        bg = s3ServiceImpl.uploadFile(requestDto.getWarehouseBackground());

        String imString = "";

        imString = s3ServiceImpl.uploadFile(requestDto.getWarehouseImage());

        warehouse.setWarehouseBackground(bg);
        warehouse.setWarehouseImage(imString);
        wareHouseRepo.save(warehouse);

    }

    @Override
    public void updateWareHouse(Warehouse wh, Integer id){
        Warehouse warehouse = wareHouseRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException("WareHouse", "wareHouse id", id.toString());
        });
        if (wh.getAddress() != null)
            warehouse.setAddress(wh.getAddress());
        if (wh.getName() != null)
            warehouse.setName(wh.getName());
        wareHouseRepo.save(warehouse);
    }

    @Override
    public WareHouseDto getWarehouseByOwner(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouses = wareHouseRepo.findByOwner(user);
        WareHouseDto warehouseDto = modelMapper.map(warehouses, WareHouseDto.class);
        warehouseDto.setFacilityList(warehouses.getFacilities().stream().map(WarehouseFacilities::getFacility)
                .collect(Collectors.toList()));

        return warehouseDto;
    }

    

    @Override
    public void deleteWarehouse(Integer id) {
        wareHouseRepo.deleteById(id);
    }

    @Override
    public List<WarehouseCardDto> getWarehouses() {
        return wareHouseRepo.findAll().stream().map(warehouse -> {
            WarehouseCardDto dto = new WarehouseCardDto();
            dto.setId(warehouse.getId());
            dto.setName(warehouse.getName());
            dto.setAddress(warehouse.getAddress());
            dto.setWarehouseImage(warehouse.getWarehouseImage());
            dto.setOwnership(warehouse.getOwnership());
            // Convert storages to a map using Stream API
            Map<String, Double> storagesMap = warehouse.getStorages().stream()
                    .collect(Collectors.toMap(
                            storage -> storage.getStorageType().toString(),
                            Storage::getPricePerKg,
                            (existingValue, newValue) -> existingValue)); // In case of duplicate keys, keep the
                                                                          // existing value
            dto.setStorages(storagesMap);
            return dto;
        }).collect(Collectors.toList()); // Use collect to convert the stream back to a list
    }

    @Override
    public List<StorageBookingsDto> getBookings(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<StorageBookingsDto> bookings = new ArrayList<>();
        wareHouseRepo.findByOwner(user).getStorages().forEach(storage -> {
            storage.getStorageBookings().forEach(booking -> {
                StorageBookingsDto bookingDto = modelMapper.map(booking, StorageBookingsDto.class);
                bookingDto.setStorageType(storage.getStorageType());
                bookingDto.setCrop(
                        productionRepo.findByToken(booking.getProductionToken())
                                .orElseThrow(() -> new ResourceNotFoundException("Production", "token",
                                        booking.getProductionToken().toString()))
                                .getCrop());

                bookings.add(bookingDto);
            });
        });

        return bookings;
    }

}
