package com.farmover.server.farmover.payloads.records;

import java.sql.Date;

public record WarehouseMonthlySalesRecord(
        Double amount,
        Date date,
        String company,
        String type) {

}
