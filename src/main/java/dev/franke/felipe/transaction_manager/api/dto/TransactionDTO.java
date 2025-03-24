package dev.franke.felipe.transaction_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record TransactionDTO(
        @JsonProperty("Id") UUID id,
        @JsonProperty("OrderId") String orderId,
        @JsonProperty("AcquirerTransactionId") String acquirerTransactionId,
        @JsonProperty("PaymentMethod") String paymentType,
        @JsonProperty("AcquirerPaymentId") String paymentId,
        @JsonProperty("AcquirerPaymentStatus") Integer paymentStatus) {}
