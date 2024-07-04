package com.farmover.server.farmover.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.ServicesDashboardDto;
import com.farmover.server.farmover.payloads.ServicesDto;
import com.farmover.server.farmover.payloads.request.ServicesRequestDto;
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
    private ObjectMapper objectMapper;

    @Override
    public ServicesDto addService(ServicesRequestDto servicesRequestDto)
            throws IOException {
        Services service = modelMapper.map(servicesRequestDto, Services.class);

        User user = userRepo.findByEmail(servicesRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", servicesRequestDto.getEmail()));

        service.setOwner(user);

        List<String> features = objectMapper.readValue(servicesRequestDto.getFeatures(),
                new TypeReference<List<String>>() {
                });

        service.setFeatures(features);

        if (servicesRequestDto.getAvailability().equals("true")) {
            service.setAvailability(true);
        } else {
            service.setAvailability(false);
        }

        service.setPricePerHour(Double.parseDouble(servicesRequestDto.getPricePerHour()));

        String imageUrl = s3Service.uploadFile(servicesRequestDto.getServiceImage());
        service.setServiceImage(imageUrl);

        Services savedService = servicesRepo.save(service);

        return modelMapper.map(savedService, ServicesDto.class);
    }

    @Override
    public ServicesDto updateService(ServicesDto servicesDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateService'");
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
            servicesDashboardDto.setAvailability(service.isAvailability());

            return servicesDashboardDto;
        }).toList();
    }

}
