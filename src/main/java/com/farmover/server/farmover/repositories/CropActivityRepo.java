package com.farmover.server.farmover.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.CropActivity;

public interface CropActivityRepo extends JpaRepository<CropActivity, Integer> {

    
}
