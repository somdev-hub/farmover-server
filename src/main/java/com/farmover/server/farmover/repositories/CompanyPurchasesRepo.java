package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.farmover.server.farmover.entities.Company;
import com.farmover.server.farmover.entities.CompanyPurchases;

public interface CompanyPurchasesRepo extends JpaRepository<CompanyPurchases, Integer> {
    List<CompanyPurchases> findByCompany(Company company);

    @Query("SELECT ws FROM CompanyPurchases ws WHERE ws.company = :company  AND MONTH(ws.purchaseDate) = MONTH(CURRENT_DATE) AND YEAR(ws.purchaseDate) = YEAR(CURRENT_DATE)")
    List<CompanyPurchases> findByCompanyAndMonth(Company company);


    
}
