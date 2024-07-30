package com.farmover.server.farmover.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.StorageType;
import com.farmover.server.farmover.payloads.CompanyDto;
import com.farmover.server.farmover.payloads.CompanyPurchasesDto;
import com.farmover.server.farmover.payloads.CompanyWarehouseCardDto;
import com.farmover.server.farmover.payloads.FarmerItem;
import com.farmover.server.farmover.payloads.records.AvailableCropWarehouseCard;
import com.farmover.server.farmover.payloads.records.RegisteredFarmersInfo;
import com.farmover.server.farmover.payloads.request.CompanyRegisterDto;
import com.farmover.server.farmover.services.impl.CompanyServiceImpl;

@RestController
@CrossOrigin
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyServiceImpl companyService;

    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<CompanyDto> addCompany(@ModelAttribute CompanyRegisterDto dto, @RequestParam String email)
            throws IOException {

        CompanyDto registerCompany = companyService.registerCompany(dto, email);

        return new ResponseEntity<CompanyDto>(registerCompany, HttpStatus.CREATED);
    }

    @GetMapping("/each-item")
    public ResponseEntity<List<CompanyWarehouseCardDto>> getWarehouseWithItems() {
        return new ResponseEntity<List<CompanyWarehouseCardDto>>(companyService.getWarehouseCardDtos(),
                HttpStatus.OK);
    }

    @GetMapping("/warehouse-market/{id}")
    public ResponseEntity<Map<StorageType, Map<Crops, ArrayList<FarmerItem>>>> getWarehouseMarket(
            @PathVariable Integer id) {
        return new ResponseEntity<Map<StorageType, Map<Crops, ArrayList<FarmerItem>>>>(
                companyService.getWarehouseMarketData(id),
                HttpStatus.OK);
    }

    @GetMapping("/warehouse/farmers/{id}")
    public ResponseEntity<List<RegisteredFarmersInfo>> getRegisteredFarmers(@PathVariable Integer id) {
        return new ResponseEntity<List<RegisteredFarmersInfo>>(companyService.getRegisteredFarmers(id), HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseItems(@RequestBody Map<Integer, Double> productionTokens,
            @RequestParam String email) {

        companyService.purchaseItems(productionTokens, email);

        return new ResponseEntity<>("Complete", HttpStatus.OK);
    }

    @GetMapping("/warehouses/crops")
    public ResponseEntity<Map<Crops, List<AvailableCropWarehouseCard>>> getWarehousesByCrops() {
        return new ResponseEntity<Map<Crops, List<AvailableCropWarehouseCard>>>(
                companyService.getWarehousesByAvailableCrops(),
                HttpStatus.OK);
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<CompanyPurchasesDto>> getCompanyPurchases(@RequestParam String email) {
        return new ResponseEntity<List<CompanyPurchasesDto>>(companyService.getCompanyPurchases(email), HttpStatus.OK);
    }

    @GetMapping("/crop-cards")
    public ResponseEntity<Map<Crops, int[]>> getCropCards(@RequestParam String email) {
        return new ResponseEntity<Map<Crops, int[]>>(companyService.getCompanyCropCards(email), HttpStatus.OK);
    }

}
