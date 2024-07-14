package com.farmover.server.farmover.repositories;


import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WareHouseRepo extends JpaRepository<Warehouse, Integer> {
    ArrayList<Warehouse> findByOwner(User user);
    
}
