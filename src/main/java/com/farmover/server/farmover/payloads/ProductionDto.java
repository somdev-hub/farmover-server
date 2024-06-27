package com.farmover.server.farmover.payloads;

import java.sql.Date;
import java.util.List;

import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.Warehouse;

import lombok.Data;

@Data
public class ProductionDto {

    private Integer token;

    private Crops crop;

    private Long quantity;

    private Date date;

    private UserDto farmer;

    private List<Services> services;

    private String status;

    private Warehouse warehouse;
}
