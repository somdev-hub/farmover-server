package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ContractDetailsDto {

    private Integer id;

    private LocalDate contractSignDate;

    private Integer duration;

    private Double price;

    private String farmer;

    private String address;

    private String phone;

    private String status;

}
