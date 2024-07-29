package com.farmover.server.farmover.payloads.records;

import com.farmover.server.farmover.entities.Crops;

public record AvailableCropWarehouseCard(Integer id, String name, String address, String warehouseImage, Crops crop,
        String phone,
        Double price) {

}
