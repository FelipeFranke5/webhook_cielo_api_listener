package dev.franke.felipe.transaction_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionRequestDTO(
        @JsonProperty("OrderId") String orderId,
        @JsonProperty("AcquirerTransactionId") String acquirerTransactionId,
        @JsonProperty("PaymentMethod") String paymentType,
        @JsonProperty("AcquirerPaymentId") String paymentId,
        @JsonProperty("AcquirerPaymentStatus") Integer paymentStatus) {}
