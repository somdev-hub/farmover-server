package com.farmover.server.farmover.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "storage_bookings")
@Data
public class StorageBookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Storage storage;

    private LocalDate bookingDate;

    private Integer bookingDuration;

    private Double bookedWeight;

    private Double bookedPrice;

    private Double itemPrice;

    private String itemUnit;

    private Boolean markForSale;

    private String status;

    private Integer productionToken;

    private String clientEmail;
}
