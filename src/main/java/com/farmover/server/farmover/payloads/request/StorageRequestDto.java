package com.farmover.server.farmover.payloads.request;

import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class StorageRequestDto {

    private StorageType storageType;

    private Double capacity; // in tons

    private Double temperature; // in celsius

    private String areaNumber;

    private Double pricePerKg;

    private String suitableFor;

}
