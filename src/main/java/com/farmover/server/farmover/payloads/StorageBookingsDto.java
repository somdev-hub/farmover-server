package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class StorageBookingsDto {

    private Integer id;

    private StorageType storageType;

    private LocalDate bookingDate;

    private Integer bookingDuration;

    private Double bookedWeight;

    private Double bookedPrice;

    private Double itemPrice;

    private String itemUnit;

    private Boolean markForSale;

    private String status;

    private Crops crop;

    private String clientEmail;
}
