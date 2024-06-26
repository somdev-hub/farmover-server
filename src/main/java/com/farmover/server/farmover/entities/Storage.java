package com.farmover.server.farmover.entities;

import java.util.List;

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
@Table(name = "storage")
@Data
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private Integer capacity; // in tons

    private String temperature; // in celsius

    private String areaNumber;

    private List<String> suitableFor;

    private Double pricePerKg;

    @ManyToOne
    private Warehouse warehouse;
}
