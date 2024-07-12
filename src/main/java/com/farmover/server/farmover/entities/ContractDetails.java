package com.farmover.server.farmover.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "contract_details")
@Data
public class ContractDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate contractSignDate;

    private Integer duration;

    private Double price;

    private String farmer;

    private String address;

    private String phone;

    private String status;

    @ManyToOne
    private Services service;

}
