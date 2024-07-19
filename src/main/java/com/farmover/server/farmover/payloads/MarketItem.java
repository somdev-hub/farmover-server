package com.farmover.server.farmover.payloads;

import java.util.List;

import com.farmover.server.farmover.entities.Crops;

import lombok.Data;

@Data
public class MarketItem {
    private Crops crop;

    List<FarmerItem> farmerItems;
}