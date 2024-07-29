package com.farmover.server.farmover.payloads.records;

import com.farmover.server.farmover.entities.Crops;

public record CompanyCropCard(Crops crop, Double price, Double quantity) {

}
