package com.manza.transactions.controller;

import com.manza.transactions.dto.PaymentDto;
import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.enumerate.PaymentProcessed;
import com.manza.transactions.exception.TransactionNotFoundException;
import com.manza.transactions.service.PaymentsService;
import com.manza.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class TransactionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PaymentsService paymentsService;

    @PostMapping(path = "/transactions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> transactions(@RequestBody TransactionDto transactionDto) {

        transactionDto = transactionService.addTransaction(transactionDto);

        URI transactionLocation = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(transactionDto.getTransactionId()).toUri();
        return ResponseEntity.created(transactionLocation).build();
    }

    @GetMapping(path = "/transactions/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TransactionDto> get(@PathVariable Long id){
        try {
            return new ResponseEntity<>(transactionService.findByTransactionId(id), HttpStatus.OK);
        } catch (TransactionNotFoundException te) {
            LOGGER.info("GET Transaction by ID not found. Message : {}", te.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error("Exception at GET Transaction by ID. Message : {}", e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/payments", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> payments(@RequestBody List<PaymentDto> paymentDtoList) {
        try {
            return new ResponseEntity<>(paymentDtoList.stream().peek( paymentDto -> {
                try {
                    paymentsService.processPayments(paymentDto);
                } catch (Exception e) {
                    LOGGER.error("Exception at POST payments. Message : {}", e.getMessage());
                    paymentDto.setPaymentProcessed(PaymentProcessed.ERROR);
                    paymentDto.setMessage(e.getMessage());
                }
            }).collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e){
            LOGGER.error("Exception at POST payments. Message : {}", e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
