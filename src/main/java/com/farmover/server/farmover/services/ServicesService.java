package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;

import com.farmover.server.farmover.payloads.ContractDetailsDto;
import com.farmover.server.farmover.payloads.ServicesDashboardDto;
import com.farmover.server.farmover.payloads.ServicesDto;
import com.farmover.server.farmover.payloads.request.ServicesRequestDto;

public interface ServicesService {

    public ServicesDto addService(ServicesRequestDto servicesRequestDto) throws IOException;

    public ServicesDto updateService(ServicesDto servicesDto);

    public ServicesDto getService(Integer id);

    public void deleteService(Integer id);

    public List<ServicesDashboardDto> getDashboardServices(String email);

    public List<ServicesDashboardDto> getServicesByOwner(String email);

    public List<ServicesDashboardDto> getAvailableServices();

    public List<ContractDetailsDto> getContractDetails(String email);
}
