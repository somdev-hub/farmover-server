package com.farmover.server.farmover.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;

public interface WareHouseRepo extends JpaRepository<Warehouse, Integer> {
    Warehouse findByOwner(User user);

}
