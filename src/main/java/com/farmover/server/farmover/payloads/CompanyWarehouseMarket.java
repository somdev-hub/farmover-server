package com.farmover.server.farmover.payloads;

import java.util.List;

import com.farmover.server.farmover.entities.StorageType;

import lombok.Data;

@Data
public class CompanyWarehouseMarket {

    private StorageType storageType;

    private List<MarketItem> marketItems;
}
