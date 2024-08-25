package com.farmover.server.farmover.payloads.records;

import java.time.LocalDate;

import com.farmover.server.farmover.entities.Crops;

public record ProductionServicesUsageRecord(
        String serviceName,
        Crops crop,
        LocalDate startDate,
        Integer duration,
        Double cost,
        Integer token,
        String status) {

}
