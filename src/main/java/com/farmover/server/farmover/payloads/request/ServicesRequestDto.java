package com.farmover.server.farmover.payloads.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ServicesRequestDto {
    private Integer id;

    private String email;

    private String status;

    private String serviceName;

    private String serviceType;

    private String serviceDescription;

    private MultipartFile serviceImage;

    private String pricePerDay;

    private String features;

    private String machineType;

    private String machineLoad;

    private String fuelType;

    // private List<ProductionDto> productions;
}
