package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import com.farmover.server.farmover.entities.ServiceStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServicesDashboardDto {
    private Integer id;

    private ServiceStatus status;

    private String serviceName;

    private String serviceType;

    private String serviceImage;

    private LocalDate lastOperated;

    private Double pricePerDay;

    private String owner;

}
