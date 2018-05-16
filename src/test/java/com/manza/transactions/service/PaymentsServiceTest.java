package com.manza.transactions.service;

import com.manza.transactions.application.Application;
import com.manza.transactions.dto.PaymentDto;
import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.enumerate.PaymentProcessed;
import com.manza.transactions.model.OperationType;
import com.manza.transactions.model.PaymentsTracking;
import com.manza.transactions.model.Transaction;
import com.manza.transactions.repository.PaymentsTrackingRepository;
import com.manza.transactions.repository.TransactionRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.manza.transactions.model.OperationType.COMPRA_A_VISTA;
import static com.manza.transactions.model.OperationType.PAGAMENTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PaymentsServiceTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    @MockBean
    private PaymentsTrackingRepository paymentsTrackingRepository;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private PaymentsService paymentsService;

    //private String payments = "[{\"account_id\": 1, \"amount\": 123.45}, {\"account_id\": 1, \"amount\": 456.78}]";

    @Test
    public void shouldProcessPaymentAndReturnZeroBalance() throws Exception {

        PaymentDto paymentDto = new PaymentDto(1L, new BigDecimal(123.45));

        TransactionDto transactionDto1 = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(1000)));

        when(transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(1L)).thenReturn(transactionDtoList);
        transactionDtoList.get(0).setBalance(new BigDecimal(1000));
        transactionDtoList.get(0).setTransactionId(1L);
        transactionDtoList.get(0).setEventDate(new Date());
        transactionDtoList.get(0).setDueDate(new Date());

        when(transactionService.addTransaction(transactionDto1)).thenReturn(transactionDto1);
        transactionDto1.setBalance(transactionDto1.getAmount());
        transactionDto1.setTransactionId(2L);
        transactionDto1.setDueDate(new Date());
        transactionDto1.setEventDate(new Date());

        Transaction transaction1 = modelMapper.map(transactionDtoList.get(0), Transaction.class);
        Transaction transaction2 = modelMapper.map(transactionDto1, Transaction.class);

        when(transactionService.saveTransaction(transactionDtoList.get(0))).thenReturn(transaction1);
        when(transactionService.saveTransaction(transactionDto1)).thenReturn(transaction2);

        PaymentsTracking paymentsTracking = new PaymentsTracking(transaction2, transaction1, transactionDto1.getAmount());

        when(paymentsTrackingRepository.save(paymentsTracking)).thenReturn(paymentsTracking);

        paymentDto = paymentsService.processPayments(paymentDto);

        assertThat(paymentDto.getPaymentProcessed()).isEqualTo(PaymentProcessed.OK);
        Assert.assertTrue(paymentDto.getAmount().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void shouldProcessPaymentAndReturnBalance() throws Exception {

        PaymentDto paymentDto = new PaymentDto(1L, new BigDecimal(1500));

        TransactionDto transactionDto1 = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(1000)));

        when(transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(1L)).thenReturn(transactionDtoList);
        transactionDtoList.get(0).setBalance(new BigDecimal(1000));
        transactionDtoList.get(0).setTransactionId(1L);
        transactionDtoList.get(0).setEventDate(new Date());
        transactionDtoList.get(0).setDueDate(new Date());

        when(transactionService.addTransaction(transactionDto1)).thenReturn(transactionDto1);
        transactionDto1.setBalance(transactionDto1.getAmount());
        transactionDto1.setTransactionId(2L);
        transactionDto1.setDueDate(new Date());
        transactionDto1.setEventDate(new Date());

        Transaction transaction1 = modelMapper.map(transactionDtoList.get(0), Transaction.class);
        Transaction transaction2 = modelMapper.map(transactionDto1, Transaction.class);

        when(transactionService.saveTransaction(transactionDtoList.get(0))).thenReturn(transaction1);
        when(transactionService.saveTransaction(transactionDto1)).thenReturn(transaction2);

        PaymentsTracking paymentsTracking = new PaymentsTracking(transaction2, transaction1, transactionDto1.getAmount());

        when(paymentsTrackingRepository.save(paymentsTracking)).thenReturn(paymentsTracking);

        paymentDto = paymentsService.processPayments(paymentDto);

        Assert.assertTrue(paymentDto.getAmount().compareTo(new BigDecimal(500)) == 0);
        assertThat(paymentDto.getPaymentProcessed()).isEqualTo(PaymentProcessed.OK);
    }


    @Test
    public void shouldProcessPaymentWithMoreThanOneDebitAndReturnZeroBalance() throws Exception {

        PaymentDto paymentDto = new PaymentDto(1L, new BigDecimal(1500));

        TransactionDto transactionDto1 = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(1000)));
        transactionDtoList.add(new TransactionDto(2L, COMPRA_A_VISTA, new BigDecimal(500)));

        when(transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(1L)).thenReturn(transactionDtoList);

        transactionDtoList.get(0).setBalance(new BigDecimal(1000));
        transactionDtoList.get(0).setTransactionId(1L);
        transactionDtoList.get(0).setEventDate(new Date());
        transactionDtoList.get(0).setDueDate(new Date());

        transactionDtoList.get(1).setBalance(new BigDecimal(500));
        transactionDtoList.get(1).setTransactionId(2L);
        transactionDtoList.get(1).setEventDate(new Date());
        transactionDtoList.get(1).setDueDate(new Date());

        when(transactionService.addTransaction(transactionDto1)).thenReturn(transactionDto1);
        transactionDto1.setBalance(transactionDto1.getAmount());
        transactionDto1.setTransactionId(3L);
        transactionDto1.setDueDate(new Date());
        transactionDto1.setEventDate(new Date());

        Transaction transaction1 = modelMapper.map(transactionDtoList.get(0), Transaction.class);
        Transaction transaction2 = modelMapper.map(transactionDto1, Transaction.class);
        Transaction transaction3 = modelMapper.map(transactionDtoList.get(1), Transaction.class);

        when(transactionService.saveTransaction(transactionDtoList.get(0))).thenReturn(transaction1);
        when(transactionService.saveTransaction(transactionDtoList.get(1))).thenReturn(transaction3);
        when(transactionService.saveTransaction(transactionDto1)).thenReturn(transaction2);

        PaymentsTracking paymentsTracking = new PaymentsTracking(transaction2, transaction1, transactionDto1.getAmount());

        when(paymentsTrackingRepository.save(paymentsTracking)).thenReturn(paymentsTracking);

        paymentDto = paymentsService.processPayments(paymentDto);

        Assert.assertTrue(paymentDto.getAmount().compareTo(BigDecimal.ZERO) == 0);
        assertThat(paymentDto.getPaymentProcessed()).isEqualTo(PaymentProcessed.OK);
    }

    @Test
    public void shouldProcessPaymentWithMoreThanOneDebitAndReturnBalance() throws Exception {

        PaymentDto paymentDto = new PaymentDto(1L, new BigDecimal(1600));

        TransactionDto transactionDto1 = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(1000)));
        transactionDtoList.add(new TransactionDto(2L, COMPRA_A_VISTA, new BigDecimal(500)));

        when(transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(1L)).thenReturn(transactionDtoList);

        transactionDtoList.get(0).setBalance(new BigDecimal(1000));
        transactionDtoList.get(0).setTransactionId(1L);
        transactionDtoList.get(0).setEventDate(new Date());
        transactionDtoList.get(0).setDueDate(new Date());

        transactionDtoList.get(1).setBalance(new BigDecimal(500));
        transactionDtoList.get(1).setTransactionId(2L);
        transactionDtoList.get(1).setEventDate(new Date());
        transactionDtoList.get(1).setDueDate(new Date());

        when(transactionService.addTransaction(transactionDto1)).thenReturn(transactionDto1);
        transactionDto1.setBalance(transactionDto1.getAmount());
        transactionDto1.setTransactionId(3L);
        transactionDto1.setDueDate(new Date());
        transactionDto1.setEventDate(new Date());

        Transaction transaction1 = modelMapper.map(transactionDtoList.get(0), Transaction.class);
        Transaction transaction2 = modelMapper.map(transactionDto1, Transaction.class);
        Transaction transaction3 = modelMapper.map(transactionDtoList.get(1), Transaction.class);

        when(transactionService.saveTransaction(transactionDtoList.get(0))).thenReturn(transaction1);
        when(transactionService.saveTransaction(transactionDtoList.get(1))).thenReturn(transaction3);
        when(transactionService.saveTransaction(transactionDto1)).thenReturn(transaction2);

        PaymentsTracking paymentsTracking = new PaymentsTracking(transaction2, transaction1, transactionDto1.getAmount());

        when(paymentsTrackingRepository.save(paymentsTracking)).thenReturn(paymentsTracking);

        paymentDto = paymentsService.processPayments(paymentDto);

        Assert.assertTrue(paymentDto.getAmount().compareTo(new BigDecimal(100)) == 0);
        assertThat(paymentDto.getPaymentProcessed()).isEqualTo(PaymentProcessed.OK);
    }

    @Test
    public void shouldProcessPaymentWithMoreThanOneDebitAndReturnZeroBalanceAndDebitWithAmount() throws Exception {

        PaymentDto paymentDto = new PaymentDto(1L, new BigDecimal(1400));

        TransactionDto transactionDto1 = new TransactionDto(paymentDto.getAccountId(), PAGAMENTO, paymentDto.getAmount());

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(1000)));
        transactionDtoList.add(new TransactionDto(2L, COMPRA_A_VISTA, new BigDecimal(500)));

        when(transactionService.findByAccountIdAndBalanceGreaterThanAndOperationTypeIsBetween(1L)).thenReturn(transactionDtoList);

        transactionDtoList.get(0).setBalance(new BigDecimal(1000));
        transactionDtoList.get(0).setTransactionId(1L);
        transactionDtoList.get(0).setEventDate(new Date());
        transactionDtoList.get(0).setDueDate(new Date());

        transactionDtoList.get(1).setBalance(new BigDecimal(500));
        transactionDtoList.get(1).setTransactionId(2L);
        transactionDtoList.get(1).setEventDate(new Date());
        transactionDtoList.get(1).setDueDate(new Date());

        when(transactionService.addTransaction(transactionDto1)).thenReturn(transactionDto1);
        transactionDto1.setBalance(transactionDto1.getAmount());
        transactionDto1.setTransactionId(3L);
        transactionDto1.setDueDate(new Date());
        transactionDto1.setEventDate(new Date());

        Transaction transaction1 = modelMapper.map(transactionDtoList.get(0), Transaction.class);
        Transaction transaction2 = modelMapper.map(transactionDto1, Transaction.class);
        Transaction transaction3 = modelMapper.map(transactionDtoList.get(1), Transaction.class);

        when(transactionService.saveTransaction(transactionDtoList.get(0))).thenReturn(transaction1);
        when(transactionService.saveTransaction(transactionDtoList.get(1))).thenReturn(transaction3);
        when(transactionService.saveTransaction(transactionDto1)).thenReturn(transaction2);

        PaymentsTracking paymentsTracking = new PaymentsTracking(transaction2, transaction1, transactionDto1.getAmount());

        when(paymentsTrackingRepository.save(paymentsTracking)).thenReturn(paymentsTracking);

        paymentDto = paymentsService.processPayments(paymentDto);

        Assert.assertTrue(paymentDto.getAmount().compareTo(BigDecimal.ZERO) == 0);
        assertThat(paymentDto.getPaymentProcessed()).isEqualTo(PaymentProcessed.OK);
    }
}
