package com.farmover.server.farmover.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.CropWiseProduction;
import com.farmover.server.farmover.payloads.ProductionDto;
import com.farmover.server.farmover.payloads.records.OrderOverview;
import com.farmover.server.farmover.payloads.records.ProductionSalesDataRecord;
import com.farmover.server.farmover.payloads.records.ProductionServicesUsageRecord;
import com.farmover.server.farmover.payloads.records.ProductionWarehouseRecord;
import com.farmover.server.farmover.payloads.request.AddProductionToWarehouseDto;
import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;
import com.farmover.server.farmover.payloads.request.AddServicesToProductionDto;
import com.farmover.server.farmover.services.impl.PaymentServiceImpl;
import com.farmover.server.farmover.services.impl.ProductionServiceImpl;
import com.stripe.exception.StripeException;

@RestController
@CrossOrigin
@RequestMapping("/production")
public class ProductionController {

    @Autowired
    private ProductionServiceImpl productionService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @PostMapping("/{email}")
    public ResponseEntity<ProductionDto> addProduction(@RequestBody ProductionDto productionDto,
            @PathVariable String email) {
        ProductionDto production = productionService.addProduction(productionDto, email);

        return new ResponseEntity<ProductionDto>(production, HttpStatus.CREATED);
    }

    @GetMapping("/{token}")
    public ResponseEntity<ProductionDto> getProduction(@PathVariable Integer token, @RequestParam String email) {
        ProductionDto production = productionService.getProduction(token, email);

        return new ResponseEntity<ProductionDto>(production, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<ProductionDto>> getFarmerProductions(@RequestParam String email,
            @RequestParam Integer page, @RequestParam Integer size) {

        Page<ProductionDto> productions = productionService.getProductionByFarmer(email, page, size);

        return new ResponseEntity<Page<ProductionDto>>(productions, HttpStatus.OK);
    }

    @GetMapping("/crops")
    public ResponseEntity<List<CropWiseProduction>> getProductionCropWise(@RequestParam String email) {

        List<CropWiseProduction> cropWiseProductions = productionService.getCropWiseProduction(email);

        return new ResponseEntity<List<CropWiseProduction>>(cropWiseProductions, HttpStatus.OK);
    }

    @PutMapping("/{token}")
    public ResponseEntity<ProductionDto> updateProduction(@RequestBody ProductionDto productionDto,
            @PathVariable Integer token) {
        ProductionDto production = productionService.updateProduction(productionDto, token);

        return new ResponseEntity<ProductionDto>(production, HttpStatus.OK);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<?> deleteProduction(@PathVariable Integer token) {
        productionService.deleteProduction(token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add-service")
    public ResponseEntity<?> addServiceToProduction(@RequestBody AddServiceToProductionDto addServiceToProductionDto)
            throws StripeException {

        if (isPaymentValid(addServiceToProductionDto.getSessionId())) {
            productionService.addServiceToProduction(addServiceToProductionDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/add-services")
    public ResponseEntity<?> addServicesToProduction(@RequestBody AddServicesToProductionDto addServicesToProductionDto)
            throws StripeException {

        if (isPaymentValid(addServicesToProductionDto.getSessionId())) {
            productionService.addServicesToProduction(addServicesToProductionDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/add-warehouse")
    public ResponseEntity<String> addToWarehouse(@RequestBody AddProductionToWarehouseDto dto,
            @RequestParam String email) throws StripeException {

        if (isPaymentValid(dto.getSessionId())) {
            productionService.addProductionToWarehouse(dto, email);
            return new ResponseEntity<String>(HttpStatus.CREATED);
        }
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/sales-report")
    public ResponseEntity<List<ProductionSalesDataRecord>> getProductionSales(@RequestParam String email) {
        return new ResponseEntity<List<ProductionSalesDataRecord>>(productionService.getSalesData(email),
                HttpStatus.OK);
    }

    @GetMapping("/overview")
    public ResponseEntity<Page<OrderOverview>> getOrderOverview(@RequestParam String email, @RequestParam Integer page,
            @RequestParam Integer size) {
        return new ResponseEntity<Page<OrderOverview>>(productionService.getOrderOverview(email, page, size),
                HttpStatus.OK);
    }

    @GetMapping("/service-usage")
    public ResponseEntity<List<ProductionServicesUsageRecord>> getServiceUsage(@RequestParam String email) {
        return new ResponseEntity<List<ProductionServicesUsageRecord>>(productionService.getServiceUsage(email),
                HttpStatus.OK);
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<ProductionWarehouseRecord>> getUsedWarehouses(@RequestParam String email) {
        return new ResponseEntity<List<ProductionWarehouseRecord>>(productionService.getUsedWarehouses(email),
                HttpStatus.OK);
    }

    private Boolean isPaymentValid(String sessionId) throws StripeException {
        Map<String, Object> paymentVerification = paymentService.verifyPayment(sessionId);
        return paymentVerification.get("paymentStatus").equals("paid");
    }

}
