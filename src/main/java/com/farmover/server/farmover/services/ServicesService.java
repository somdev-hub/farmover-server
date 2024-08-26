package com.farmover.server.farmover.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.farmover.server.farmover.payloads.ContractDetailsDto;
import com.farmover.server.farmover.payloads.ServicesDashboardDto;
import com.farmover.server.farmover.payloads.ServicesDto;
import com.farmover.server.farmover.payloads.request.ServicesRequestDto;

public interface ServicesService {

    public ServicesDto addService(ServicesRequestDto servicesRequestDto) throws IOException;

    public ServicesDto updateService(ServicesRequestDto servicesDto, Integer id) throws IOException;

    public Map<Integer, String> getListOfServices(String email);

    public ServicesDto getService(Integer id);

    public void deleteService(Integer id);

    public List<ServicesDashboardDto> getDashboardServices(String email);

    public List<ServicesDashboardDto> getServicesByOwner(String email);

    public List<ServicesDashboardDto> getAvailableServices();

    public List<ContractDetailsDto> getContractDetails(String email);

    public void updateServiceStatus(Integer id, String status, Integer productionToken);

}
