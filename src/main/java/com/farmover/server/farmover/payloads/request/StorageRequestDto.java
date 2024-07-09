package com.farmover.server.farmover.payloads.request;



import org.springframework.web.multipart.MultipartFile;


import lombok.Data;
@Data
public class StorageRequestDto {
   
    private String storageType;

    private String capacity; // in tons

    private String temperature; // in celsius

    private String areaNumber;

    private String pricePerKg;

    private MultipartFile img;
}
