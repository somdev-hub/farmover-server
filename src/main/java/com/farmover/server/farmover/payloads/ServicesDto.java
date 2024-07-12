package com.farmover.server.farmover.payloads;

import java.time.LocalDate;
import java.util.List;

import com.farmover.server.farmover.entities.ServiceStatus;

import lombok.Data;

@Data
public class ServicesDto {

    private Integer id;

    private UserDto owner;

    private ServiceStatus status;

    private String serviceName;

    private String serviceType;

    private String serviceDescription;

    private String serviceImage;

    private Double pricePerDay;

    private List<ServiceFeaturesDto> features;

    private String machineType;

    private String machineLoad;

    private String fuelType;

    private LocalDate commencedDate;

    private LocalDate lastOperated;

    private LocalDate lastRepaired;

}
