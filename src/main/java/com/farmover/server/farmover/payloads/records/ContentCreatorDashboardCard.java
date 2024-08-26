package com.farmover.server.farmover.payloads.records;

public record ContentCreatorDashboardCard(
        String type,
        String title,
        Integer views,
        Integer upvotes,
        Integer comments) {

}
