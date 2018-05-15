package com.manza.transactions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class TransactionDto {

    private Long transactionId;
    private Long accountId;
    private Integer operationTypeId;
    private BigDecimal amount;
    private BigDecimal balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date eventDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dueDate;

    public TransactionDto() {
    }

    public TransactionDto(Long accountId, Integer operationTypeId, BigDecimal amount) {
        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDto that = (TransactionDto) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(operationTypeId, that.operationTypeId) &&
                amount.compareTo(that.amount) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(accountId, operationTypeId, amount);
    }
}
