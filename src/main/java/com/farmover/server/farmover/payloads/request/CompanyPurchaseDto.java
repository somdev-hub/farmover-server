package com.farmover.server.farmover.payloads.request;

import java.util.Map;

import lombok.Data;

@Data
public class CompanyPurchaseDto {
    private Map<Integer, Double> productionTokens;

    private String sessionId;

}
