package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import lombok.Data;


@Data
public class CropActivityDto {

    private Integer id;

    private String activityTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer activityNumber;

    // private ProductionDto productionDto;
}
