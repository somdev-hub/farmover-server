package com.farmover.server.farmover.payloads;

import java.sql.Date;
import java.util.List;

import com.farmover.server.farmover.payloads.records.VideoCommentRecord;

import lombok.Data;

@Data
public class RecentsCommentsDto {

    private String title;

    private Date date;

    private String type;

    private List<VideoCommentRecord> comments;
}
