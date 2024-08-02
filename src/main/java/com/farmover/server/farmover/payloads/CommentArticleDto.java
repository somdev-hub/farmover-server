package com.farmover.server.farmover.payloads;

import java.sql.Date;

import lombok.Data;

@Data
public class CommentArticleDto {

    private String name;

    private String profilePicture;

    private String comment;

    private Date date;
}
