package com.manza.transactions.repository;

import com.manza.transactions.model.OperationType;
import com.manza.transactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(Long id);
    List<Transaction> findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(
            Long accountId,
            BigDecimal balance,
            OperationType operationType,
            OperationType operationType1);
}
