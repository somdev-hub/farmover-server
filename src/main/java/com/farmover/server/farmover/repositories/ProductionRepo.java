package com.farmover.server.farmover.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.Crops;
import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;

public interface ProductionRepo extends JpaRepository<Production, Integer> {
        Optional<List<Production>> findByFarmer(User farmer);

        Optional<Page<Production>> findByFarmer(User farmer, Pageable pageable);

        Optional<Production> findByToken(Integer token);

        Optional<Production> findByStatus(String status);

        Optional<Production> findByWarehouse(Warehouse warehouse);

        Optional<Production> findByCrop(Crops crop);

        Optional<Production> findByTokenAndFarmer(Integer token, User user);
}
