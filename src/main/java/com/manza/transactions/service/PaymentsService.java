package com.manza.transactions.service;

import com.manza.transactions.dto.PaymentDto;

import java.util.List;

public interface PaymentsService {

    List<PaymentDto> processPayments(List<PaymentDto> paymentDtoList) throws Exception;

}
