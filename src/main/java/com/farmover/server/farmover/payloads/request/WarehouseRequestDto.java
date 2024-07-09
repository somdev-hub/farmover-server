package com.farmover.server.farmover.payloads.request;



import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class WarehouseRequestDto {


    private String name;

    private String address;
    
    private MultipartFile warehouseImage;

    private MultipartFile warehouseBackground;

}
