package com.farmover.server.farmover.payloads.records;

import java.sql.Date;

public record OrderOverview(
        Double total,
        String from,
        Date date,
        String type
) {

}
