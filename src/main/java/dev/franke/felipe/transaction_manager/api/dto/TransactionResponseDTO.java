package dev.franke.felipe.transaction_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionResponseDTO(
        @JsonProperty("Data") TransactionDTO data,
        @JsonProperty("CreatedAt") OffsetDateTime createdAt,
        @JsonProperty("RecordId") UUID id) {}
