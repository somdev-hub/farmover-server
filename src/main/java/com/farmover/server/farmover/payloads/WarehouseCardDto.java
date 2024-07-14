package com.farmover.server.farmover.payloads;

import java.util.List;
import java.util.Map;

import com.farmover.server.farmover.entities.WarehouseOwnership;

import lombok.Data;

@Data
public class WarehouseCardDto {
    private Integer id;

    private String name;

    private String address;

    private String warehouseImage;

    private WarehouseOwnership ownership;

    private Map<String, Double> storages;
}
