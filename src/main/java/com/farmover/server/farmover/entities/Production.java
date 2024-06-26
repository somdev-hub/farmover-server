package com.farmover.server.farmover.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String crop;

    private String quentity;

    private Date date;

    @ManyToOne
    private User farmer;

    private String services;

    private String status;

    @ManyToOne
    private Warehouse warehouse;

}
