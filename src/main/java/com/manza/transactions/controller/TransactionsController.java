package com.manza.transactions.controller;

import com.manza.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class TransactionsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/transactions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> transactions(@RequestBody String body) {
        return new ResponseEntity<>("transactions", HttpStatus.OK);
    }

    @PostMapping(path = "/payments", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> payments(@RequestBody String body) {
        return new ResponseEntity<>("payments", HttpStatus.OK);
    }
}
