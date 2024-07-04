package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class ServicesDashboardDto {
    private Integer id;

    private boolean availability;

    private String serviceName;

    private String serviceType;

}
