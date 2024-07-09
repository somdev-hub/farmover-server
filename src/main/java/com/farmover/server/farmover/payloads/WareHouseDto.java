package com.farmover.server.farmover.payloads;

import lombok.Data;

@Data
public class WareHouseDto {
    private Integer id;

    private String name;

    private String address;
    
    private String warehouseImage;

    private String warehouseBackground;
}
