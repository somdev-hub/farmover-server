package com.farmover.server.farmover.payloads.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class VideoRequestDto {

    MultipartFile video;

    String description;

    String title;

    MultipartFile thumbnail;

    String longDescription;
}
