package com.manza.transactions.service;

import com.manza.transactions.dto.TransactionDto;

public interface TransactionService {

    TransactionDto addTransaction(TransactionDto transactionDto);
    TransactionDto findByTransactionId(Long transactionId) throws Exception;

}
