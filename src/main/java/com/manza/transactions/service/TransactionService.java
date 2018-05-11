package com.manza.transactions.service;

import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.model.Transaction;

import java.util.List;

public interface TransactionService {

    TransactionDto addTransaction(TransactionDto transactionDto);
    TransactionDto findByTransactionId(Long transactionId) throws Exception;
    List<TransactionDto> findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(Long accountId);
    Transaction saveTransaction(TransactionDto transactiondto);

}
