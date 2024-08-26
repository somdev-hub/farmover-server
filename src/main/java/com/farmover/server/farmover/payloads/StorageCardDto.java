package com.farmover.server.farmover.payloads;

import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class StorageCardDto {

    private Integer id;

    private StorageType storageType;

    private Double capacity; // in tons

    private Double availableCapacity;
}
