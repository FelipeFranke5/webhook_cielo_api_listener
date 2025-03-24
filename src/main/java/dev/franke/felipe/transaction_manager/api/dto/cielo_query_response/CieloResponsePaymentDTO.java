package dev.franke.felipe.transaction_manager.api.dto.cielo_query_response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CieloResponsePaymentDTO(
        @JsonProperty("Tid") String tid,
        @JsonProperty("Type") String type,
        @JsonProperty("PaymentId") String paymentId,
        @JsonProperty("Status") Integer status) {}
