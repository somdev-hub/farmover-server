package com.farmover.server.farmover.payloads;

import java.time.LocalDate;
import java.util.List;

import com.farmover.server.farmover.entities.Crops;

import lombok.Data;

@Data
public class ProductionDto {

    private Integer token;

    private Crops crop;

    private Long quantity;

    private LocalDate date;

    private List<CropActivityDto> cropActivities;

    // private UserDto farmer;

    private String status;

    private String warehouseName;

    private Integer warehouseId;
}
