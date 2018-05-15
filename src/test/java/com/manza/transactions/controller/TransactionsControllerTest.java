package com.manza.transactions.controller;

import com.manza.transactions.application.Application;
import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.exception.TransactionNotFoundException;
import com.manza.transactions.model.OperationType;
import com.manza.transactions.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.manza.transactions.model.OperationType.COMPRA_A_VISTA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class TransactionsControllerTest {

    private TransactionDto transactionDto = new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(123.45));
    private TransactionDto transactionDtoSaved = new TransactionDto(1L, COMPRA_A_VISTA, new BigDecimal(123.45));
    private static final String content = "{\"account_id\": 1, \"operation_type_id\": 1, \"amount\": 123.45}";
    private static final String transactionsUrl = "/v1/transactions";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void shouldReturnRedirectedUrlTransactionSaved() throws Exception {
        transactionDtoSaved.setTransactionId(1L);

        when(transactionService.addTransaction(transactionDto)).thenReturn(transactionDtoSaved);

        mockMvc.perform(MockMvcRequestBuilders.post(transactionsUrl)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(content))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path(transactionsUrl +"/" + transactionDtoSaved.getTransactionId().toString())
                        .toUriString())
                );
        ;
    }

    @Test
    public void shouldReturnTransactionDto() throws Exception {
        transactionDtoSaved.setTransactionId(1L);
        transactionDtoSaved.setBalance(new BigDecimal(123.45));
        Calendar calendar = Calendar.getInstance();
        transactionDtoSaved.setEventDate(calendar.getTime());
        calendar.add(Calendar.DATE, 30);
        transactionDtoSaved.setDueDate(calendar.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        when(transactionService.findByTransactionId(1L)).thenReturn(transactionDtoSaved);

        mockMvc.perform(MockMvcRequestBuilders.get(transactionsUrl+"/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction_id")
                        .value(transactionDtoSaved.getTransactionId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account_id")
                        .value(transactionDtoSaved.getAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.operation_type_id")
                        .value(transactionDtoSaved.getOperationTypeId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount")
                        .value(transactionDtoSaved.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance")
                        .value(transactionDtoSaved.getBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.event_date")
                        .value(new SimpleDateFormat("yyyy-MM-dd").format(transactionDtoSaved.getEventDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.due_date")
                        .value(new SimpleDateFormat("yyyy-MM-dd").format(transactionDtoSaved.getDueDate())));

    }

    @Test
    public void shouldReturnNoContent() throws Exception {
        when(transactionService.findByTransactionId(1L)).thenThrow(new TransactionNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get(transactionsUrl+"/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnInternalServErrror() throws Exception {
        when(transactionService.findByTransactionId(1L)).thenThrow(new Exception());

        mockMvc.perform(MockMvcRequestBuilders.get(transactionsUrl+"/1").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isInternalServerError());
    }

}
