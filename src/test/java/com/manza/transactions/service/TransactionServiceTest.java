package com.manza.transactions.service;

import com.manza.transactions.application.Application;
import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.model.OperationType;
import com.manza.transactions.model.Transaction;
import com.manza.transactions.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static com.manza.transactions.model.OperationType.COMPRA_A_VISTA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransactionServiceTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void shouldAddTransaction() {
        Calendar calendar = Calendar.getInstance();
        Date eventDate = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date dueDate = calendar.getTime();

        Transaction transaction = new Transaction(
                1L,
                new OperationType(COMPRA_A_VISTA, "compra a vista", 2L),
                new BigDecimal(123.45),
                new BigDecimal(123.45),
                eventDate,
                dueDate);
        transaction.setTransactionId(1L);

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionDto transactionDto = transactionService.addTransaction(modelMapper.map(transaction, TransactionDto.class));
        assertThat(transactionDto.getTransactionId()).isEqualTo(transaction.getTransactionId());
    }
}
