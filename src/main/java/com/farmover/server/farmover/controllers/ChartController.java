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

    @GetMapping("/weekly-service-usage")
    public ResponseEntity<Map<String, Integer>> getWeeklyUsage(@RequestParam String email) {
        return new ResponseEntity<Map<String, Integer>>(chartServices.getWeeklyServiceUsage(email), HttpStatus.OK);
    }

    @GetMapping("/production")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getProduction(@RequestParam String email) {
        return new ResponseEntity<Map<String, Long>>(chartServices.getMonthlyProductionTally(email), HttpStatus.OK);
    }

    @GetMapping("/warehouse-usage")
    public ResponseEntity<Map<String, Double>> getWarehouseUsage(@RequestParam String email) {
        return new ResponseEntity<Map<String, Double>>(chartServices.getWarehouseUsageChart(email), HttpStatus.OK);
    }

    @GetMapping("/warehouse-revenue/bookings")
    public ResponseEntity<Map<String, Double>> getWarehouseRevenueFromBookings(@RequestParam String email) {
        return new ResponseEntity<Map<String, Double>>(chartServices.getWarehouseRevenueFromBookings(email),
                HttpStatus.OK);
    }

    @GetMapping("/warehouse-revenue/sales")
    public ResponseEntity<Map<String, Double>> getWarehouseRevenueFromSales(@RequestParam String email) {
        return new ResponseEntity<Map<String, Double>>(chartServices.getWarehouseRevenueFromSales(email),
                HttpStatus.OK);
    }

    @GetMapping("/each-service-usage")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<Map<String, Double>> getEachServiceUsage(@RequestParam String email) {
        return new ResponseEntity<Map<String, Double>>(chartServices.getServiceRevenues(email), HttpStatus.OK);
    }

    @GetMapping("/content-creator/views-by-roles")
    public ResponseEntity<Map<String, Map<String, Integer>>> getViewsByRoles(@RequestParam String email) {
        return new ResponseEntity<Map<String, Map<String, Integer>>>(chartServices.getViewCountByRoles(email),
                HttpStatus.OK);
    }

    @GetMapping("/content-creator/views-by-month")
    public ResponseEntity<Map<String, Map<String, Integer>>> getViewsByMonths(@RequestParam String email) {
        return new ResponseEntity<Map<String, Map<String, Integer>>>(chartServices.getViewsCountByMonths(email),
                HttpStatus.OK);
    }

    @GetMapping("/content-creator/engagements-by-roles")
    public ResponseEntity<Map<String, Map<String, Integer>>> getEngagementsByRoles(@RequestParam String email) {
        return new ResponseEntity<Map<String, Map<String, Integer>>>(chartServices.getEngagementsCountByRoles(email),
                HttpStatus.OK);
    }

    @GetMapping("/company-purchases")
    public ResponseEntity<Map<String, Map<String, Double>>> getCompanyPurchases(@RequestParam String email) {
        return new ResponseEntity<Map<String, Map<String, Double>>>(chartServices.getCompanyMonthlyPurchases(email),
                HttpStatus.OK);
    }

    @GetMapping("/company-quantity")
    public ResponseEntity<Map<String, Map<String, Double>>> getCompanyQuantity(@RequestParam String email) {
        return new ResponseEntity<Map<String, Map<String, Double>>>(chartServices.getCompanyMonthlyQuantity(email),
                HttpStatus.OK);
    }

}
