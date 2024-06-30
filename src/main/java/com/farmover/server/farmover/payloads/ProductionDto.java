package com.farmover.server.farmover.payloads;

import java.time.LocalDate;
import java.util.List;

import com.farmover.server.farmover.entities.CropActivity;
import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.Warehouse;

import lombok.Data;

@Data
public class ProductionDto {

    private Integer token;

    private Crops crop;

    private Long quantity;

    private LocalDate date;

    private List<CropActivityDto> cropActivities;

    // private UserDto farmer;

    private List<Services> services;

    private String status;

    private Warehouse warehouse;
}
