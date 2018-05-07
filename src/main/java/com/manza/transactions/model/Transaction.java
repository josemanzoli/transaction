package com.manza.transactions.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    private Long accountId;
    @ManyToOne
    @JoinColumn(name = "operation_type_id")
    private OperationType operationType;
    private BigDecimal amount;
    private BigDecimal balance;
    private Date eventDate;
    private Date dueDate;

    public Transaction() {
    }

    public Transaction(Long accountId, OperationType operationType, BigDecimal amount, BigDecimal balance, Date eventDate, Date dueDate) {
        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = amount;
        this.balance = balance;
        this.eventDate = eventDate;
        this.dueDate = dueDate;
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

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "transactionId=" + transactionId +
                ", accountId=" + accountId +
                ", operationType=" + operationType +
                ", amount=" + amount +
                ", balance=" + balance +
                ", eventDate=" + eventDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
