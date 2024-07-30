package com.farmover.server.farmover.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.StorageBookings;

public interface StorageBookingsRepo extends JpaRepository<StorageBookings, Integer> {
    Optional<StorageBookings> findStorageBookingsByProductionToken(Integer token);

    Optional<List<StorageBookings>> findByClientEmail(String email);
}
