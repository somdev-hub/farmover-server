package com.farmover.server.farmover.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.SuitableFor;

public interface SuitableForRepo extends JpaRepository<SuitableFor,Integer>{
    List<SuitableFor> findByStorage(Storage storage);
    
}
