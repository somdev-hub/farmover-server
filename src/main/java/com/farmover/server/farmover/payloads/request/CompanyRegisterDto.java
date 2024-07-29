package com.farmover.server.farmover.payloads.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CompanyRegisterDto {
    private String name;

    private String companyDetails;
    
    private String address;
    
    private String pin;
    
    private String ownership;
    
    private String companyIndustry;
    
    private MultipartFile companyImage;

}
