package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class CompanyDto {

    private Integer id;

    private String name;

    private String address;

    private String pin;

    private String companyDetails;

    private String companyImage;

    private String companyIndustry;

    private String ownership;
}
