package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class ArticleDto {

    private Integer id;

    private String authorName;

    private String title;

    private String date;

    private String thumbnail;

    private String subHeading;
}
