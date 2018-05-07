package com.manza.transactions.repository;

import com.manza.transactions.model.PaymentsTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentsTrackingRepository extends JpaRepository<PaymentsTracking, Long> {

    Optional<PaymentsTracking> findByPaymentsTrackingId(Long id);
}
