package com.farmover.server.farmover.payloads.records;

public record VideoUserResponseRecord(
        Integer id,
        String thumbnail,
        String title,
        String authorName,
        String authorProfile,
        String description) {

}
