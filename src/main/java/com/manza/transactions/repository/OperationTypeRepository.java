package com.manza.transactions.repository;

import com.manza.transactions.model.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {

    Optional<OperationType> findByOperationTypeId(Long id);
}
