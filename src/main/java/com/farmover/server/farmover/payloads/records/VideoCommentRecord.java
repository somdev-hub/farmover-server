package com.farmover.server.farmover.payloads.records;

import java.sql.Date;

public record VideoCommentRecord(
        Integer id,
        String name,
        String profileImage,
        String comment,
        Date date) {

}
