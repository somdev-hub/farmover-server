package com.farmover.server.farmover.payloads;


import lombok.Data;

@Data
public class VideoDto {
    private Integer id;
    private String authorName;
    private String title;
    private String url;
    private String date;
    private String description;
    private String thumbnail;
    private String longDescription;
}
