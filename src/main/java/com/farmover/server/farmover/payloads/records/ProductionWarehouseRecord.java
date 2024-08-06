package com.farmover.server.farmover.payloads.records;

import java.time.LocalDate;

import com.farmover.server.farmover.entities.Crops;

public record ProductionWarehouseRecord(
        String warehouseName,
        Crops crop,
        Integer productionToken,
        Double quantity,
        String unit,
        LocalDate date,
        Double cost,
        Integer duration
) {

}
