package com.farmover.server.farmover.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "production")
@Data
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer token;

    @Enumerated(EnumType.STRING)
    private Crops crop;

    private String quantity;

    private Date date;

    @ManyToOne
    private User farmer;

    @ManyToMany(mappedBy = "productions")
    private List<Services> services;

    private String status;

    @ManyToOne
    private Warehouse warehouse;

}
