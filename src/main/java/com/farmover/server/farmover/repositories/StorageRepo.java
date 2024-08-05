package com.farmover.server.farmover.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.Storage;

import com.farmover.server.farmover.entities.Warehouse;

public interface StorageRepo extends JpaRepository<Storage, Integer> {
    ArrayList<Storage> findByWarehouse(Warehouse warehouse);
}
