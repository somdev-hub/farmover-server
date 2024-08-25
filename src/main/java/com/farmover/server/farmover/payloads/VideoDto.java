package com.farmover.server.farmover.payloads;

import java.util.List;

import lombok.Data;

@Data
public class VideoDto {
    private Integer id;
    private String authorName;
    private String authorProfile;
    private String title;
    private String url;
    private String date;
    private String description;
    private String thumbnail;
    private String longDescription;
    private List<String> tags;
    private Integer upCount;
    private Integer downCount;
    private Integer viewCount;
}
