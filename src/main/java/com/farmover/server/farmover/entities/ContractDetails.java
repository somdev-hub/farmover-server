package com.farmover.server.farmover.entities;

import java.time.LocalDate;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
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

    @NonNull
    private LocalDate contractSignDate;

    @NonNull
    private Integer duration;

    @NonNull
    private Double price;

    @NonNull
    private String farmer;

    @NonNull
    private String address;

    @NonNull
    private Integer productionToken;

    @NonNull
    private String phone;

    @NonNull
    private String status;

    @ManyToOne
    private Services service;

}
