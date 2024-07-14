package com.farmover.server.farmover.payloads.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class WarehouseRequestDto {

    private String name;

    private String address;

    private String pin;

    private String warehouseDetails;

    private String ownership;

    private MultipartFile warehouseImage;

    private MultipartFile warehouseBackground;

    private String facilities;

}
