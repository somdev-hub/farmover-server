package com.farmover.server.farmover.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.Company;

public interface CompanyRepo extends JpaRepository<Company, Integer> {

}
