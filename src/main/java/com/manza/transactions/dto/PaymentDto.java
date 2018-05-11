package com.manza.transactions.dto;

import com.manza.transactions.enumerate.PaymentProcessed;

import java.math.BigDecimal;

public class PaymentDto {

    private Long paymentId;
    private Long accountId;
    private BigDecimal amount;
    private PaymentProcessed paymentProcessed;
    private String message;

    public PaymentDto() {
    }

    public PaymentDto(Long accountId, BigDecimal amount) {
        this.accountId = accountId;
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);;
    }

    public PaymentProcessed getPaymentProcessed() {
        return paymentProcessed;
    }

    public void setPaymentProcessed(PaymentProcessed paymentProcessed) {
        this.paymentProcessed = paymentProcessed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
