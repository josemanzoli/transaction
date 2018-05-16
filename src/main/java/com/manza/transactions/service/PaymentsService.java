package com.manza.transactions.service;

import com.manza.transactions.dto.PaymentDto;

import java.util.List;

public interface PaymentsService {

    PaymentDto processPayments(PaymentDto paymentDto) throws Exception;

}
