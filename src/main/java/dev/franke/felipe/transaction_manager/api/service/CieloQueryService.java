package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.CredentialsException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CieloQueryService {

    @Value("${cielo.api.url.query}")
    private String baseURL;

    @Value("${cielo.api.merchant.id}")
    private String merchantId;

    @Value("${cielo.api.merchant.key}")
    private String merchantKey;

    private RestTemplate restTemplate;

    public CieloQueryService() {
        this.setRestTemplate();
    }

    void checkCredentials() {
        List<String> errors = new ArrayList<>();

        if (this.getMerchantId() == null || this.getMerchantKey() == null) {
            errors.add("MerchantId or MerchantKey is required");
            throw new CredentialsException("Errors: [" + errors + "]");
        }

        if (this.getMerchantId().isBlank() || this.getMerchantKey().isBlank()) {
            errors.add("MerchantId or MerchantKey should not be blank");
        }

        if (this.getMerchantId().length() != 36) {
            errors.add("MerchantId should have 36 characters");
        }

        if (this.getMerchantKey().length() != 40) {
            errors.add("MerchantKey should have 40 characters");
        }

        try {
            UUID.fromString(this.getMerchantId());
        } catch (IllegalArgumentException | NullPointerException exception) {
            errors.add("MerchantId should be in UUID format");
        }

        if (!errors.isEmpty()) {
            throw new CredentialsException("Errors: [" + errors + "]");
        }
    }

    void checkURL() {
        if (this.getBaseURL() == null
                || this.getBaseURL().isBlank()
                || !this.getBaseURL().contains("cieloecommerce.cielo.com.br/1/sales")) {
            throw new CredentialsException("INVALID URL");
        }
    }

    public CompletableFuture<CieloResponseDTO> runTransactionTask(String paymentId) {
        return CompletableFuture.supplyAsync(() -> getTransaction(paymentId));
    }

    public CieloResponseDTO getTransaction(String paymentId) {
        checkCredentials();
        checkURL();

        if (paymentId == null || paymentId.isBlank() || paymentId.length() < 36) {
            return null;
        }

        try {
            UUID.fromString(paymentId);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }

        String uri = baseURL + "/" + paymentId;

        try {
            return this.getRestTemplate().getForObject(uri, CieloResponseDTO.class);
        } catch (RestClientException restClientException) {
            return null;
        }
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    private void setRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        restTemplate = builder.defaultHeader("MerchantId", this.getMerchantId())
                .defaultHeader("MerchantKey", this.getMerchantKey())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
