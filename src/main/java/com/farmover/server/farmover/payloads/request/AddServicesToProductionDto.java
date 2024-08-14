package com.farmover.server.farmover.payloads.request;

import java.util.List;

import lombok.Data;

@Data
public class AddServicesToProductionDto {

    private Integer productionToken;

    private List<Integer> serviceIds;

    private String email;

    private List<Integer> durations;

    private String sessionId;
}
