package com.manza.transactions.service.impl;

import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.exception.TransactionNotFoundException;
import com.manza.transactions.model.OperationType;
import com.manza.transactions.model.Transaction;
import com.manza.transactions.repository.TransactionRepository;
import com.manza.transactions.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);

        transaction.setBalance(transactionDto.getAmount());

        Calendar calendar = Calendar.getInstance();
        transaction.setEventDate(calendar.getTime());

        calendar.add(Calendar.DATE, 30);
        transaction.setDueDate(calendar.getTime());

        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
    }

    @Override
    public TransactionDto findByTransactionId(Long transactionId) throws Exception{
        Transaction transaction = transactionRepository.findByTransactionId(transactionId).orElseThrow(
                () -> new TransactionNotFoundException("Transaction " + transactionId.toString() + " was not found." ));

        return modelMapper.map(transaction, TransactionDto.class);
    }

    @Override
    public List<TransactionDto> findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(Long accountId) {
        List<Transaction> transactionList = transactionRepository.
                findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(
                        accountId,
                        new BigDecimal(0),
                        new OperationType(1L),
                        new OperationType(3L))
                .stream()
                .sorted(Comparator.comparing(Transaction::getOperationType, (operationType1, operationType2) -> {
                    if (operationType1.getChargeOrder().equals(operationType2.getChargeOrder())){
                        return 0;
                    }
                    return operationType1.getChargeOrder() > operationType2.getChargeOrder() ? -1 : 1;

                })).collect(toList());
        return transactionList.stream().map(transaction -> {
            return modelMapper.map(transaction, TransactionDto.class);
        }).collect(toList());
    }

    @Override
    public Transaction saveTransaction(TransactionDto transactionDto) {
        return transactionRepository.save(modelMapper.map(transactionDto, Transaction.class));
    }
}
