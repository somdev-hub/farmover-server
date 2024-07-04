package com.farmover.server.farmover.payloads.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ServicesRequestDto {
    private Integer id;

    private String email;

    private String availability;

    private String serviceName;

    private String serviceType;

    private String serviceDescription;

    private MultipartFile serviceImage;

    private String pricePerHour;

    private String features;

    private String machineType;

    private String machineLoad;

    private String fuelType;

    // private List<ProductionDto> productions;
}
