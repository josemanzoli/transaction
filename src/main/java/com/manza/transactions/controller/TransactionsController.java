package com.manza.transactions.controller;

import com.manza.transactions.dto.TransactionDto;
import com.manza.transactions.exception.TransactionNotFoundException;
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

@RestController
@RequestMapping("/v1")
public class TransactionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    private TransactionService transactionService;

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
    public ResponseEntity<String> payments(@RequestBody String body) {
        return new ResponseEntity<>("payments", HttpStatus.OK);
    }
}
