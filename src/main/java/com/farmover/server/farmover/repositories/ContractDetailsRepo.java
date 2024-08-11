package com.farmover.server.farmover.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmover.server.farmover.entities.ContractDetails;
import com.farmover.server.farmover.entities.Services;

public interface ContractDetailsRepo extends JpaRepository<ContractDetails, Integer> {
    Optional<List<ContractDetails>> findByFarmer(String email);

    @Query("SELECT ws FROM ContractDetails ws WHERE ws.service = :service  AND MONTH(ws.contractSignDate) = MONTH(CURRENT_DATE) AND YEAR(ws.contractSignDate) = YEAR(CURRENT_DATE)")
    Optional<List<ContractDetails>> findByServiceAndCurrentMonth(@Param("service") Services service);

}
