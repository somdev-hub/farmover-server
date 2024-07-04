package com.farmover.server.farmover.payloads;

import java.util.List;

import lombok.Data;

@Data
public class ServicesDto {

    private Integer id;

    private UserDto owner;

    private boolean availability;

    private String serviceName;

    private String serviceType;

    private String serviceDescription;

    private String serviceImage;

    private Double pricePerHour;

    private List<String> features;

    private String machineType;

    private String machineLoad;

    private String fuelType;

    private List<ProductionDto> productions;
}
