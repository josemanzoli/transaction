package com.manza.transactions.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OperationType {

    public static final Integer COMPRA_A_VISTA = 1;
    public static final Integer COMPRA_PARCELADA = 2;
    public static final Integer SAQUE = 3;
    public static final Integer PAGAMENTO = 4;

    @Id
    private Integer operationTypeId;
    private String description;
    private Long chargeOrder;

    public OperationType() {
    }

    public OperationType(Integer operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public OperationType(Integer operationTypeId, String description, Long chargeOrder) {
        this.operationTypeId = operationTypeId;
        this.description = description;
        this.chargeOrder = chargeOrder;
    }

    public Integer getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Integer operationTypeId) {
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
