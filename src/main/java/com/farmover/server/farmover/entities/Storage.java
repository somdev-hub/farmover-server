package com.farmover.server.farmover.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

// to do normalize the suitable for
@Entity
@Table(name = "storage")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private Double capacity; // in tons

    private Double temperature; // in celsius

    private String areaNumber;

    private Double pricePerKg;

    @ManyToOne
    @JsonIgnoreProperties("storages")
    private Warehouse warehouse;

    private String suitableFor;

    private Double availableCapacity;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StorageBookings> storageBookings;

}
