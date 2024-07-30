package com.farmover.server.farmover.entities;

import java.sql.Date;

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
@Table(name = "warehouse_sales")
@Data
public class WarehouseSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String buyer;

    @Enumerated(EnumType.STRING)
    private Crops crop;

    private Date date;

    private Double quantity;

    private String unit;

    private Double price;

    private Double commission;

    private Integer productionToken;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @ManyToOne
    private Warehouse warehouse;
}
