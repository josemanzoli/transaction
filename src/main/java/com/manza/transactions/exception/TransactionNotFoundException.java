package com.manza.transactions.exception;

public class TransactionNotFoundException extends Exception {

    public TransactionNotFoundException(){

    }

    public TransactionNotFoundException(String message){
        super(message);
    }
}
