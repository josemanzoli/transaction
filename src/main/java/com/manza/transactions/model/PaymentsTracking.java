package com.manza.transactions.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class PaymentsTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentsTrackingId;
    @ManyToOne
    @JoinColumn(name = "credit_transaction_id")
    private Transaction creditTransaction;
    @ManyToOne
    @JoinColumn(name = "debit_transaction_id")
    private Transaction debitTransaction;
    private BigDecimal amount;

    public PaymentsTracking() {
    }

    public PaymentsTracking(Transaction creditTransaction, Transaction debitTransaction, BigDecimal amount) {
        this.creditTransaction = creditTransaction;
        this.debitTransaction = debitTransaction;
        this.amount = amount;
    }

    public Long getPaymentsTrackingId() {
        return paymentsTrackingId;
    }

    public void setPaymentsTrackingId(Long paymentsTrackingId) {
        this.paymentsTrackingId = paymentsTrackingId;
    }

    public Transaction getCreditTransaction() {
        return creditTransaction;
    }

    public void setCreditTransaction(Transaction creditTransaction) {
        this.creditTransaction = creditTransaction;
    }

    public Transaction getDebitTransaction() {
        return debitTransaction;
    }

    public void setDebitTransaction(Transaction debitTransaction) {
        this.debitTransaction = debitTransaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentsTracking that = (PaymentsTracking) o;
        return Objects.equals(paymentsTrackingId, that.paymentsTrackingId) &&
                Objects.equals(creditTransaction, that.creditTransaction) &&
                Objects.equals(debitTransaction, that.debitTransaction) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(paymentsTrackingId, creditTransaction, debitTransaction, amount);
    }

    @Override
    public String toString() {
        return "PaymentsTracking{" +
                "paymentsTrackingId=" + paymentsTrackingId +
                ", creditTransaction=" + creditTransaction +
                ", debitTransaction=" + debitTransaction +
                ", amount=" + amount +
                '}';
    }
}
