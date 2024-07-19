package com.farmover.server.farmover.payloads;

import java.util.List;

import com.farmover.server.farmover.entities.Crops;

import lombok.Data;

@Data
public class CompanyWarehouseCardDto {

    private Integer id;

    private String name;

    private String address;

    private String warehouseImage;

    private List<Crops> availableItems;
}
