package com.farmover.server.farmover.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmover.server.farmover.entities.PaymentSessionId;

public interface PaymentSessionIdRepo extends JpaRepository<PaymentSessionId, Integer> {
    Optional<PaymentSessionId> findBySessionId(String sessionId);
}
