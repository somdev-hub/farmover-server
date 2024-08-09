package com.farmover.server.farmover.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.ContractDetailsDto;
import com.farmover.server.farmover.payloads.ServicesDashboardDto;
import com.farmover.server.farmover.payloads.ServicesDto;
import com.farmover.server.farmover.payloads.request.ServicesRequestDto;
import com.farmover.server.farmover.services.impl.ServicesServiceImpl;

@RestController
@RequestMapping("/services")
@CrossOrigin
@PreAuthorize("hasRole('SERVICE_PROVIDER')")
public class ServicesController {

    @Autowired
    private ServicesServiceImpl servicesService;

    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<ServicesDto> addService(@ModelAttribute ServicesRequestDto servicesRequestDto)
            throws IOException {
        ServicesDto addedService = servicesService.addService(servicesRequestDto);
        return new ResponseEntity<ServicesDto>(addedService, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<Integer, String>> getListOfServices(@RequestParam String email) {
        return new ResponseEntity<Map<Integer, String>>(servicesService.getListOfServices(email), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicesDto> getService(@PathVariable Integer id) {
        ServicesDto service = servicesService.getService(id);
        return new ResponseEntity<ServicesDto>(service, HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<ServicesDashboardDto>> getDashboardServices(@RequestParam String email) {
        return new ResponseEntity<List<ServicesDashboardDto>>(servicesService.getDashboardServices(email),
                HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<ServicesDashboardDto>> getServicesByOwner(@RequestParam String email) {
        return new ResponseEntity<List<ServicesDashboardDto>>(servicesService.getServicesByOwner(email), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ServicesDashboardDto>> getAvailableServices() {
        return new ResponseEntity<List<ServicesDashboardDto>>(servicesService.getAvailableServices(), HttpStatus.OK);
    }

    @GetMapping("/contract-details/")
    public ResponseEntity<List<ContractDetailsDto>> getContractDetails(@RequestParam String email) {
        return new ResponseEntity<List<ContractDetailsDto>>(servicesService.getContractDetails(email), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ServicesDto> updateService(@PathVariable Integer id, @ModelAttribute ServicesRequestDto dto)
            throws NumberFormatException, IOException {

        return new ResponseEntity<ServicesDto>(servicesService.updateService(dto, id), HttpStatus.OK);
    }

}
