package com.farmover.server.farmover.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.services.impl.ProductionServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    private ProductionServiceImpl productionService;

    @PostMapping("/{email}")
    public ResponseEntity<ProductionDto> addProduction(@RequestBody ProductionDto productionDto,
            @PathVariable String email) {
        ProductionDto production = productionService.addProduction(productionDto, email);

        return new ResponseEntity<ProductionDto>(production, HttpStatus.CREATED);
    }

    @GetMapping("/{token}")
    public ResponseEntity<ProductionDto> getProduction(@PathVariable Integer token) {
        ProductionDto production = productionService.getProduction(token);

        return new ResponseEntity<ProductionDto>(production, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductionDto>> getFarmerProductions(@RequestParam String email) {

        List<ProductionDto> productions = productionService.getProductionByFarmer(email);

        return new ResponseEntity<List<ProductionDto>>(productions, HttpStatus.OK);
    }

    @GetMapping("/crops")
    public ResponseEntity<List<CropWiseProduction>> getProductionCropWise(@RequestParam String email) {

        List<CropWiseProduction> cropWiseProductions = productionService.getCropWiseProduction(email);

        return new ResponseEntity<List<CropWiseProduction>>(cropWiseProductions, HttpStatus.OK);
    }
}
