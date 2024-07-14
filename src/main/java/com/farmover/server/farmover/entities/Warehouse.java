package com.farmover.server.farmover.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "warehouses")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    private String pin;

    @Column(length = 5000)
    private String warehouseDetails;

    private String warehouseImage;

    private String warehouseBackground;

    @Enumerated(EnumType.STRING)
    private WarehouseOwnership ownership;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseFacilities> facilities;

    @OneToOne
    @JsonIgnoreProperties("managedWarehouses")
    private User owner;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("warehouse")
    private List<Storage> storages;

}
