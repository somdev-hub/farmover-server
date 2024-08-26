package com.farmover.server.farmover.payloads.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ArticleRequest {

    String title;

    String tags;

    String subHeading;

    MultipartFile thumbnail;

    String content;
}
