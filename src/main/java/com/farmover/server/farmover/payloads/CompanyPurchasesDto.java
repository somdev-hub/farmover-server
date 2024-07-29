package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import com.farmover.server.farmover.entities.Crops;

import lombok.Data;

@Data
public class CompanyPurchasesDto {
    private Integer id;

    private LocalDate purchaseDate;

    private Double purchaseQuantity;

    private Double purchasePrice;

    private Double purchaseTotal;

    private String status;

    private String warehouseName;

    private Crops crop;
}
