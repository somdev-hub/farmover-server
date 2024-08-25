package com.farmover.server.farmover.payloads;

import java.util.List;

import com.farmover.server.farmover.entities.WarehouseOwnership;

import lombok.Data;

@Data
public class WareHouseDto {

    private Integer id;

    private String name;

    private String address;

    private String pin;

    private String warehouseDetails;

    private String warehouseImage;

    private String warehouseBackground;

    private WarehouseOwnership ownership;

    private List<String> facilityList;

    private UserDto owner;

    private List<StorageDto> storages;

}
