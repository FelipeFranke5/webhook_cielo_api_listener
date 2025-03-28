package dev.franke.felipe.transaction_manager.api.model;

import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponsePaymentDTO;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "acquirer_transactions")
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "acquirer_transaction_id")
    private String acquirerTransactionId;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_status")
    private Integer paymentStatus;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public TransactionModel() {}

    private TransactionModel(TransactionModelBuilder builder) {
        this.setOrderId(builder.orderId);
        this.setAcquirerTransactionId(builder.acquirerTransactionId);
        this.setPaymentType(builder.paymentType);
        this.setPaymentId(builder.paymentId);
        this.setPaymentStatus(builder.paymentStatus);
        this.setCreatedAt(builder.createdAt);
    }

    public TransactionModel(
            UUID id,
            String orderId,
            String acquirerTransactionId,
            String paymentType,
            String paymentId,
            Integer paymentStatus,
            OffsetDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.acquirerTransactionId = acquirerTransactionId;
        this.paymentType = paymentType;
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static CieloResponseDTO toCieloResponse(TransactionModel transaction) {
        return new CieloResponseDTO(
                transaction.getOrderId(),
                new CieloResponsePaymentDTO(
                        transaction.getAcquirerTransactionId(),
                        transaction.getPaymentType(),
                        transaction.getPaymentId(),
                        transaction.getPaymentStatus()));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TransactionModel that)) return false;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getOrderId(), that.getOrderId())
                && Objects.equals(getAcquirerTransactionId(), that.getAcquirerTransactionId())
                && Objects.equals(getPaymentType(), that.getPaymentType())
                && Objects.equals(getPaymentId(), that.getPaymentId())
                && Objects.equals(getPaymentStatus(), that.getPaymentStatus())
                && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getOrderId(),
                getAcquirerTransactionId(),
                getPaymentType(),
                getPaymentId(),
                getPaymentStatus(),
                getCreatedAt());
    }

    @Override
    public String toString() {
        return "TransactionModel{" + "id="
                + id + ", orderId='"
                + orderId + '\'' + ", acquirerTransactionId='"
                + acquirerTransactionId + '\'' + ", paymentType='"
                + paymentType + '\'' + ", paymentId='"
                + paymentId + '\'' + ", paymentStatus="
                + paymentStatus + ", createdAt="
                + createdAt + '}';
    }

    public static class TransactionModelBuilder {

        private String orderId;
        private String acquirerTransactionId;
        private String paymentType;
        private String paymentId;
        private Integer paymentStatus;
        private OffsetDateTime createdAt;

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

        public TransactionModelBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TransactionModel build() {
            return new TransactionModel(this);
        }
    }
}
