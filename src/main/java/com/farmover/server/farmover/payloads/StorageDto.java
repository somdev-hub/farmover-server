package com.farmover.server.farmover.payloads;

import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class StorageDto {

    private Integer id;

    private StorageType storageType;

    private Double capacity; // in tons

    private Double temperature; // in celsius

    private String areaNumber;

    private String suitableFor;

    private Double pricePerKg;

    private Double availableCapacity;

}
