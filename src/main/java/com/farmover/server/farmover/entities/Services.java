package com.farmover.server.farmover.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "services")
@Data
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User owner;

    private boolean availability;

    private String serviceName;

    private String serviceType;

    private String serviceDescription;

    private String serviceImage;

    private Double pricePerHour;

    private List<String> features;

    private String machineType;

    private String machineLoad;

    private String fuelType;

    @ManyToMany
    @JoinTable(name = "services_productions", joinColumns = @JoinColumn(name = "service_id"), inverseJoinColumns = @JoinColumn(name = "production_id"))
    private List<Production> productions;
}
