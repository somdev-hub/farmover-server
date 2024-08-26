package com.farmover.server.farmover.payloads;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CalendarEventsDto {

    private String title;
    private LocalDate start;
    private LocalDate end;
}
