package com.farmover.server.farmover.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    private String serviceName;

    private String serviceType;

    @Column(length = 5000)
    private String serviceDescription;

    private String serviceImage;

    private Double pricePerDay;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceFeatures> features;

    private String machineType;

    private String machineLoad;

    private String fuelType;

    private LocalDate commencedDate;

    private LocalDate lastOperated;

    private LocalDate lastRepaired;

    @OneToMany(mappedBy = "services", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceRepair> serviceRepairs;

    @ManyToMany
    @JoinTable(name = "services_productions", joinColumns = @JoinColumn(name = "service_id"), inverseJoinColumns = @JoinColumn(name = "production_id"))
    private List<Production> productions;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContractDetails> contractDetails;
}
