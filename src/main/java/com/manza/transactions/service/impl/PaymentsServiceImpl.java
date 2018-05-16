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
    public PaymentDto processPayments(PaymentDto paymentDto) throws Exception {
        //find the transactions (COMPRA_A_VISTA, COMPRA_PARCELADA, SAQUE) in the database that are not with 0.00 balance
        List<TransactionDto> transactionList = transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(paymentDto.getAccountId());

        //create a payment transaction and add to database
        TransactionDto paymentTransactionDto = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());
        paymentTransactionDto.setBalance(paymentDto.getAmount());
        paymentTransactionDto = transactionService.addTransaction(paymentTransactionDto);

        //loop to the debit transactions in order to update the balance
        for (TransactionDto debitTransactionDto : transactionList){

            //only do something if the payment transaction still have funds
            if (paymentDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {

                //subtract the debit amount from the payment amount
                BigDecimal paymentBalance = paymentDto.getAmount().subtract(debitTransactionDto.getBalance());

                //if the paymentBalance <= 0 means that the payment was not enough to pay the entire balance of the debit transaction
                if (paymentBalance.compareTo(BigDecimal.ZERO) <= 0) {
                    persistPayments(paymentBalance.abs(), BigDecimal.ZERO, debitTransactionDto, paymentTransactionDto, paymentDto.getAmount());
                    paymentDto.setAmount(BigDecimal.ZERO);
                    continue;
                }

                //if the paymentBalance > 0 means that the debit amount is 0 now and I have more payment balance to keep paying
                if (paymentBalance.compareTo(BigDecimal.ZERO) > 0) {
                    persistPayments(BigDecimal.ZERO, paymentBalance, debitTransactionDto, paymentTransactionDto, debitTransactionDto.getBalance());

                    paymentDto.setAmount(paymentBalance);
                }
            }
        }
        paymentDto.setPaymentProcessed(PaymentProcessed.OK);
        return paymentDto;
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
