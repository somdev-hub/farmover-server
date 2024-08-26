package com.farmover.server.farmover.payloads.request;

import lombok.Data;

@Data
public class AddServiceToProductionDto {
    private Integer productionToken;
    private Integer serviceId;
    private String email;
    private Integer duration;
    private String sessionId;
}
