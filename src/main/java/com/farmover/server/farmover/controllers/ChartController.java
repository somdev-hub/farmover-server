package com.farmover.server.farmover.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.services.impl.ChartServicesImpl;

@RestController
@RequestMapping("/chart")
@CrossOrigin
public class ChartController {

    @Autowired
    private ChartServicesImpl chartServices;

    @GetMapping("/expenses")
    public ResponseEntity<Map<String, Double>> getExpenses(@RequestParam String email) {
        return new ResponseEntity<Map<String, Double>>(chartServices.getExpenses(email), HttpStatus.OK);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Double>> getRevenue(@RequestParam String email) {
        return new ResponseEntity<Map<String, Double>>(chartServices.getRevenue(email), HttpStatus.OK);
    }

    @GetMapping("/production")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getProduction(@RequestParam String email) {
        return new ResponseEntity<Map<String, Long>>(chartServices.getMonthlyProductionTally(email), HttpStatus.OK);
    }
}
