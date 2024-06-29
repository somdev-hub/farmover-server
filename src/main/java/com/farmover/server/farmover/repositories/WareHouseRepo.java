package com.farmover.server.farmover.repositories;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseRepo extends JpaRepository<Warehouse, Integer> {
    List<Warehouse> findByOwner(User owner);
    
}
