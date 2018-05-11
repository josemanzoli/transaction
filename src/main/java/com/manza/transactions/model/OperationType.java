package com.manza.transactions.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OperationType {

    @Id
    private Long operationTypeId;
    private String description;
    private Long chargeOrder;

    public OperationType() {
    }

    public OperationType(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public OperationType(Long operationTypeId, String description, Long chargeOrder) {
        this.operationTypeId = operationTypeId;
        this.description = description;
        this.chargeOrder = chargeOrder;
    }

    public Long getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getChargeOrder() {
        return chargeOrder;
    }

    public void setChargeOrder(Long chargeOrder) {
        this.chargeOrder = chargeOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationType that = (OperationType) o;
        return Objects.equals(operationTypeId, that.operationTypeId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(operationTypeId);
    }

    @Override
    public String toString() {
        return "OperationType{" +
                "operationTypeId=" + operationTypeId +
                ", description='" + description + '\'' +
                ", chargeOrder=" + chargeOrder +
                '}';
    }
}
