package com.farmover.server.farmover.payloads.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateUserRequestDto {

    private String uname;

    private String email;

    private String phone;

    private String address;

    private MultipartFile profileImage;
}
