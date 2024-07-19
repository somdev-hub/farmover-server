package com.farmover.server.farmover.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "company_purchases")
@Data
public class CompanyPurchases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Company company;

    private LocalDate purchaseDate;

    private Double purchaseQuantity;

    private Double purchasePrice;

    private Double purchaseTotal;

    private String status;

    // private Integer productionToken;

    @Enumerated(EnumType.STRING)
    private Crops crop;
}
