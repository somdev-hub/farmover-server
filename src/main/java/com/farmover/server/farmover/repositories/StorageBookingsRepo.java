package com.farmover.server.farmover.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.StorageBookings;

public interface StorageBookingsRepo extends JpaRepository<StorageBookings, Integer> {
    Optional<StorageBookings> findStorageBookingsByProductionToken(Integer token);

    Optional<List<StorageBookings>> findByClientEmail(String email);

    List<StorageBookings> findByStorage(Storage storage);

    List<StorageBookings> findByStorageAndBookingDate(Storage storage, LocalDate bookingDate);

    @Query("SELECT sb FROM StorageBookings sb WHERE sb.storage = :storage AND MONTH(sb.bookingDate) = MONTH(CURRENT_DATE) AND YEAR(sb.bookingDate) = YEAR(CURRENT_DATE)")
    List<StorageBookings> findBookingsByStorageWithinCurrentMonth(@Param("storage") Storage storage);
}
