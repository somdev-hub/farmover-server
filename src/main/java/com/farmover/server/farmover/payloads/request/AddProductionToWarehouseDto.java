package com.farmover.server.farmover.payloads.request;

import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class AddProductionToWarehouseDto {

    private Integer warehouseId;

    private StorageType storageType;

    private Double weight;

    private Integer duration;

    private Boolean markForSale;

    private Double minimumPrice;

    private String minimumUnit;

    private Integer productionToken;

    private String sessionId;
}
