package com.farmover.server.farmover.payloads.records;

import java.sql.Date;

import com.farmover.server.farmover.entities.Crops;

public record WarehouseSalesRecord(
        String companyName,
        Date date,
        Crops crop,
        Double quantity,
        String unit,
        Double price,
        Double commission

) {

}
