package com.manza.transactions.service.impl;

import com.manza.transactions.dto.PaymentDto;
import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.enumerate.PaymentProcessed;
import com.manza.transactions.model.PaymentsTracking;
import com.manza.transactions.model.Transaction;
import com.manza.transactions.repository.PaymentsTrackingRepository;
import com.manza.transactions.service.PaymentsService;
import com.manza.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.manza.transactions.model.OperationType.PAGAMENTO;


@Service
public class PaymentsServiceImpl implements PaymentsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsServiceImpl.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentsTrackingRepository paymentsTrackingRepository;

    @Override
    public List<PaymentDto> processPayments(List<PaymentDto> paymentDtoList) throws Exception {
        return paymentDtoList.stream().peek(paymentDto -> {

            List<TransactionDto> transactionList = transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(paymentDto.getAccountId());

            TransactionDto paymentTransactionDto = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());
            paymentTransactionDto.setBalance(paymentDto.getAmount());
            paymentTransactionDto = transactionService.addTransaction(paymentTransactionDto);

            for (TransactionDto debitTransactionDto : transactionList){

                if (paymentDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal paymentBalance = paymentDto.getAmount().subtract(debitTransactionDto.getBalance());

                    if (paymentBalance.compareTo(BigDecimal.ZERO) <= 0) {

                        if (paymentBalance.compareTo(BigDecimal.ZERO) < 0 )
                            persistPayments(paymentBalance.abs(), BigDecimal.ZERO, debitTransactionDto, paymentTransactionDto, debitTransactionDto.getBalance());
                        else
                            persistPayments(paymentBalance, paymentBalance, debitTransactionDto, paymentTransactionDto, paymentDto.getAmount());

                        paymentDto.setAmount(BigDecimal.ZERO);
                        paymentDto.setPaymentProcessed(PaymentProcessed.OK);
                        continue;
                    }

                    if (paymentBalance.compareTo(BigDecimal.ZERO) > 0) {
                        persistPayments(BigDecimal.ZERO, paymentBalance, debitTransactionDto, paymentTransactionDto, debitTransactionDto.getBalance());

                        paymentDto.setAmount(paymentBalance);
                    }
                }
            }
        }).collect(Collectors.toList());
    }

    public void persistPayments (BigDecimal debitPaymentBalance, BigDecimal paymentTransactionBalance, TransactionDto debitTransactionDto, TransactionDto paymentTransactionDto, BigDecimal amountTracking){
        //save debit transaction
        debitTransactionDto.setBalance(debitPaymentBalance);
        Transaction debitTransaction = transactionService.saveTransaction(debitTransactionDto);

        //save payment transaction
        paymentTransactionDto.setBalance(paymentTransactionBalance);
        Transaction paymentTransaction = transactionService.saveTransaction(paymentTransactionDto);

        //save payments tracking transaction
        PaymentsTracking paymentsTracking = new PaymentsTracking();
        paymentsTracking.setCreditTransaction(paymentTransaction);
        paymentsTracking.setDebitTransaction(debitTransaction);
        paymentsTracking.setAmount(amountTracking);
        paymentsTrackingRepository.save(paymentsTracking);
    }
}
