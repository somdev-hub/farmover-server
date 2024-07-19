package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import com.farmover.server.farmover.entities.Crops;

import lombok.Data;

@Data
public class FarmerItem {
    private String farmerName;

    private String farmerEmail;

    private String farmerImage;

    private Crops crop;

    private Double weight;

    private Double price;

    private String unit;

    private LocalDate bookedDate;
}
