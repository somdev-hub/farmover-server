package com.farmover.server.farmover.payloads;

import java.util.List;

import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class StorageDto {

    private Integer id;
    private StorageType storageType;

    private Integer capacity; // in tons

    private String temperature; // in celsius

    private String areaNumber;

    private List<String> suitableFor;

    private Double pricePerKg;

    private String img;
}
