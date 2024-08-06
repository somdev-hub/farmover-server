package com.farmover.server.farmover.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.ContractDetails;
import com.farmover.server.farmover.entities.User;

public interface ContractDetailsRepo extends JpaRepository<ContractDetails, Integer> {
    Optional<List<ContractDetails>> findByFarmer(String email);
}
