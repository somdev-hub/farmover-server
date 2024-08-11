package com.farmover.server.farmover.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.User;

public interface ServicesRepo extends JpaRepository<Services, Integer> {

    Optional<Services> findByServiceName(String serviceName);

    Optional<Services> findByServiceType(String serviceType);

    Optional<List<Services>> findByOwner(User owner);

}
