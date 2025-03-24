package dev.franke.felipe.transaction_manager.api.model;

import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponsePaymentDTO;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TransactionModel {

    public static CieloResponseDTO toCieloResponse(TransactionModel transaction) {
        return new CieloResponseDTO(
                transaction.getOrderId(),
                new CieloResponsePaymentDTO(
                        transaction.getAcquirerTransactionId(),
                        transaction.getPaymentType(),
                        transaction.getPaymentId(),
                        transaction.getPaymentStatus()));
    }

    public static class TransactionModelBuilder {
        private String orderId;
        private String acquirerTransactionId;
        private String paymentType;
        private String paymentId;
        private Integer paymentStatus;

        public TransactionModelBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public TransactionModelBuilder acquirerTransactionId(String acquirerTransactionId) {
            this.acquirerTransactionId = acquirerTransactionId;
            return this;
        }

        public TransactionModelBuilder paymentType(String paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public TransactionModelBuilder paymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public TransactionModelBuilder paymentStatus(Integer paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public TransactionModel build() {
            return new TransactionModel(this);
        }
    }

    public TransactionModel() {}

    private TransactionModel(TransactionModelBuilder builder) {
        this.setOrderId(builder.orderId);
        this.setAcquirerTransactionId(builder.acquirerTransactionId);
        this.setPaymentType(builder.paymentType);
        this.setPaymentId(builder.paymentId);
        this.setPaymentStatus(builder.paymentStatus);
    }

    public TransactionModel(
            UUID id,
            String orderId,
            String acquirerTransactionId,
            String paymentType,
            String paymentId,
            Integer paymentStatus) {
        this.setId(id);
        this.setOrderId(orderId);
        this.setAcquirerTransactionId(acquirerTransactionId);
        this.setPaymentType(paymentType);
        this.setPaymentId(paymentId);
        this.setPaymentStatus(paymentStatus);
    }

    public TransactionModel(
            String orderId, String acquirerTransactionId, String paymentType, String paymentId, Integer paymentStatus) {
        this.orderId = orderId;
        this.acquirerTransactionId = acquirerTransactionId;
        this.paymentType = paymentType;
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String orderId;

    @Column(nullable = false, updatable = false, unique = true)
    private String acquirerTransactionId;

    @Column(nullable = false, updatable = false)
    private String paymentType;

    @Column(nullable = false, updatable = false)
    private String paymentId;

    @Column(nullable = false, updatable = false)
    private Integer paymentStatus;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAcquirerTransactionId() {
        return acquirerTransactionId;
    }

    public void setAcquirerTransactionId(String acquirerTransactionId) {
        this.acquirerTransactionId = acquirerTransactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TransactionModel that)) return false;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getOrderId(), that.getOrderId())
                && Objects.equals(getAcquirerTransactionId(), that.getAcquirerTransactionId())
                && Objects.equals(getPaymentType(), that.getPaymentType())
                && Objects.equals(getPaymentId(), that.getPaymentId())
                && Objects.equals(getPaymentStatus(), that.getPaymentStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getOrderId(),
                getAcquirerTransactionId(),
                getPaymentType(),
                getPaymentId(),
                getPaymentStatus());
    }

    @Override
    public String toString() {
        return "TransactionModel{" + "id="
                + id + ", orderId='"
                + orderId + '\'' + ", acquirerTransactionId='"
                + acquirerTransactionId + '\'' + '}';
    }
}
