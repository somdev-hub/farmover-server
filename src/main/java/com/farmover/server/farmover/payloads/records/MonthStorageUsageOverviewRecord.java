package com.farmover.server.farmover.payloads.records;

import com.farmover.server.farmover.entities.StorageType;

public record MonthStorageUsageOverviewRecord(
        String clientName,
        StorageType storage,
        Double totalStorageUsage,
        Integer duration) {

}
