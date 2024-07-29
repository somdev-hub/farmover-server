package com.farmover.server.farmover.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.Company;
import com.farmover.server.farmover.entities.User;

public interface CompanyRepo extends JpaRepository<Company, Integer> {
    Optional<Company> findByManager(User manager);
}
