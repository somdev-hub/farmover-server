package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.ContractDetails;
import com.farmover.server.farmover.entities.ServiceFeatures;
import com.farmover.server.farmover.entities.ServiceStatus;
import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.ContractDetailsDto;
import com.farmover.server.farmover.payloads.ServicesDashboardDto;
import com.farmover.server.farmover.payloads.ServicesDto;
import com.farmover.server.farmover.payloads.request.ServicesRequestDto;
import com.farmover.server.farmover.repositories.ContractDetailsRepo;
import com.farmover.server.farmover.repositories.ServicesRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.services.ServicesService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ServicesServiceImpl implements ServicesService {

    @Autowired
    private ServicesRepo servicesRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private S3ServiceImpl s3Service;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContractDetailsRepo contractDetailsRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ServicesDto addService(ServicesRequestDto servicesRequestDto)
            throws IOException {
        Services service = modelMapper.map(servicesRequestDto, Services.class);

        User user = userRepo.findByEmail(servicesRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", servicesRequestDto.getEmail()));

        service.setOwner(user);

        List<String> featureStrings = objectMapper.readValue(servicesRequestDto.getFeatures(),
                new TypeReference<List<String>>() {
                });

        // Assuming service.getFeatures() properly initializes an empty collection
        // Adjust this part if your initialization logic is different
        List<ServiceFeatures> serviceFeatures = new ArrayList<>();
        for (String featureString : featureStrings) {
            ServiceFeatures serviceFeature = new ServiceFeatures();
            serviceFeature.setFeature(featureString);
            serviceFeature.setService(service); // Set the service in each feature
            serviceFeatures.add(serviceFeature);
            // Optionally save each ServiceFeature entity here if not cascading persist
        }

        // Set the collection of ServiceFeatures to the service
        service.setFeatures(serviceFeatures);

        service.setPricePerDay(Double.parseDouble(servicesRequestDto.getPricePerDay()));

        service.setCommencedDate(LocalDate.now());

        service.setContractDetails(new ArrayList<>());

        String imageUrl = s3Service.uploadFile(servicesRequestDto.getServiceImage());
        service.setServiceImage(imageUrl);

        // Save the service entity; ensure cascade options are properly set to also
        // persist ServiceFeatures
        Services savedService = servicesRepo.save(service);

        return modelMapper.map(savedService, ServicesDto.class);
    }

    @Override
    public ServicesDto getService(Integer id) {
        Services service = servicesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", Integer.toString(id)));

        return modelMapper.map(service, ServicesDto.class);

    }

    @Override
    public void deleteService(Integer id) {
        servicesRepo.deleteById(id);
    }

    @Override
    public List<ServicesDashboardDto> getDashboardServices(String email) {
        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Services> services = servicesRepo.findByOwner(owner)
                .orElseThrow(() -> new ResourceNotFoundException("Services", "owner", email));

        return services.stream().map(service -> {
            ServicesDashboardDto servicesDashboardDto = new ServicesDashboardDto();
            servicesDashboardDto.setId(service.getId());
            servicesDashboardDto.setServiceName(service.getServiceName());
            servicesDashboardDto.setServiceType(service.getServiceType());
            servicesDashboardDto.setStatus(service.getStatus());
            servicesDashboardDto.setServiceImage(service.getServiceImage());

            return servicesDashboardDto;
        }).toList();
    }

    @Override
    public List<ServicesDashboardDto> getServicesByOwner(String email) {
        User owner = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Services> services = servicesRepo.findByOwner(owner)
                .orElseThrow(() -> new ResourceNotFoundException("Services", "owner", email));

        List<ServicesDashboardDto> servicesDtos = services.stream().map(service -> {
            ServicesDashboardDto servicesDashboardDto = new ServicesDashboardDto();
            servicesDashboardDto.setId(service.getId());
            servicesDashboardDto.setServiceName(service.getServiceName());
            servicesDashboardDto.setServiceType(service.getServiceType());
            servicesDashboardDto.setStatus(service.getStatus());
            servicesDashboardDto.setServiceImage(service.getServiceImage());
            servicesDashboardDto.setLastOperated(service.getLastOperated());
            servicesDashboardDto.setPricePerDay(service.getPricePerDay());

            return servicesDashboardDto;
        }).toList();

        return servicesDtos;
    }

    @Override
    public List<ServicesDashboardDto> getAvailableServices() {
        List<Services> services = servicesRepo.findAll();

        List<ServicesDashboardDto> servicesDtos = services.stream().map(service -> {
            ServicesDashboardDto servicesDashboardDto = new ServicesDashboardDto();
            servicesDashboardDto.setId(service.getId());
            servicesDashboardDto.setServiceName(service.getServiceName());
            servicesDashboardDto.setServiceType(service.getServiceType());
            servicesDashboardDto.setStatus(service.getStatus());
            servicesDashboardDto.setServiceImage(service.getServiceImage());
            servicesDashboardDto.setLastOperated(service.getLastOperated());
            servicesDashboardDto.setPricePerDay(service.getPricePerDay());
            servicesDashboardDto.setOwner(service.getOwner().getUname());

            return servicesDashboardDto;
        }).toList();

        return servicesDtos;
    }

    @Override
    public List<ContractDetailsDto> getContractDetails(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Services> services = servicesRepo.findByOwner(user)
                .orElseThrow(() -> new ResourceNotFoundException("Services", "owner", email));

        List<ContractDetailsDto> allContractDetails = new ArrayList<>();

        services.stream().forEach(service -> {
            service.getContractDetails().stream().forEach(detail -> {
                ContractDetailsDto cd = new ContractDetailsDto();
                cd.setId(detail.getId());
                cd.setContractSignDate(detail.getContractSignDate());
                cd.setDuration(detail.getDuration());
                cd.setPrice(detail.getPrice());
                cd.setFarmer(detail.getFarmer());
                cd.setAddress(detail.getAddress());
                cd.setPhone(detail.getPhone());
                cd.setProductionToken(detail.getProductionToken());
                cd.setService(service.getServiceName());
                cd.setServiceStatus(service.getStatus().toString());

                allContractDetails.add(cd);
            });

        });

        return allContractDetails;
    }

    @Override
    public ServicesDto updateService(ServicesRequestDto servicesDto, Integer id) throws IOException {
        Services service = servicesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", Integer.toString(id)));

        service.setServiceName(servicesDto.getServiceName());
        service.setServiceType(servicesDto.getServiceType());
        service.setFuelType(servicesDto.getFuelType());

        List<String> featureStrings = objectMapper.readValue(servicesDto.getFeatures(),
                new TypeReference<List<String>>() {
                });

        // Clear the existing features
        service.getFeatures().clear();

        List<ServiceFeatures> serviceFeatures = new ArrayList<>();
        for (String featureString : featureStrings) {
            ServiceFeatures serviceFeature = new ServiceFeatures();
            serviceFeature.setFeature(featureString);
            serviceFeature.setService(service); // Set the service in each feature
            serviceFeatures.add(serviceFeature);
            // Optionally save each ServiceFeature entity here if not cascading persist
        }

        // Set the collection of ServiceFeatures to the service
        service.setFeatures(serviceFeatures);

        service.setPricePerDay(Double.parseDouble(servicesDto.getPricePerDay()));

        if (servicesDto.getServiceImage() != null) {
            String imageUrl = s3Service.uploadFile(servicesDto.getServiceImage());
            service.setServiceImage(imageUrl);
        }

        Services savedService = servicesRepo.save(service);

        return modelMapper.map(savedService, ServicesDto.class);
    }

    @Override
    public Map<Integer, String> getListOfServices(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Services> services = servicesRepo.findByOwner(user)
                .orElseThrow(() -> new ResourceNotFoundException("Services", "owner", email));

        return services.stream().collect(Collectors.toMap(Services::getId, Services::getServiceName));
    }

    @Override
    public void updateServiceStatus(Integer id, String status, Integer productionToken) {
        Services service = servicesRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "id", Integer.toString(id)));

        ContractDetails contractDetail = contractDetailsRepo.findByProductionToken(productionToken)
                .orElseThrow(() -> new ResourceNotFoundException("ContractDetails", "productionToken",
                        Integer.toString(productionToken)));

        contractDetail.setStatus("FINISHED");

        service.setStatus(ServiceStatus.valueOf(status));

        servicesRepo.save(service);
        contractDetailsRepo.save(contractDetail);
    }

}
