package com.farmover.server.farmover.payloads.records;

import java.sql.Date;

public record ProductionSalesDataRecord(
        String companyName,
        String crop,
        Date date,
        Double revenue) {

}
