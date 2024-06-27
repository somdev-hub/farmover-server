package com.farmover.server.farmover.payloads;

import com.farmover.server.farmover.entities.Crops;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CropWiseProduction {
    private Crops cropName;
    private Long productionQuantity;

    public CropWiseProduction(Crops cropName, Long productionQuantity) {
        this.cropName = cropName;
        this.productionQuantity = productionQuantity;
    }
}
