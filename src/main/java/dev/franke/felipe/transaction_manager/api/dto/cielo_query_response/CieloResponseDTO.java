package dev.franke.felipe.transaction_manager.api.dto.cielo_query_response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CieloResponseDTO(
        @JsonProperty("MerchantOrderId") String orderId, @JsonProperty("Payment") CieloResponsePaymentDTO payment) {}
