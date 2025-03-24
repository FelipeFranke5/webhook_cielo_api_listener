package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.CredentialsException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CieloQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CieloQueryService.class);

    @Value("${cielo.api.url.query}")
    private String baseURL;

    @Value("${cielo.api.merchant.id}")
    private String merchantId;

    @Value("${cielo.api.merchant.key}")
    private String merchantKey;

    private RestTemplate restTemplate;

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

    public CieloResponseDTO getTransaction(String paymentId, boolean test) {
        logger.info("Initializing method to call Cielo API");
        if (!test) {
            this.setRestTemplate();
        }

        checkCredentials();
        checkURL();

        if (paymentId == null || paymentId.isBlank() || paymentId.length() < 36) {
            logger.error("PaymentId is null, blank or invalid");
            logger.info("PaymentId: {}", paymentId);
            return null;
        }

        try {
            UUID.fromString(paymentId);
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("PaymentId is not valid UUID");
            logger.info("PaymentId: {}", paymentId);
            return null;
        }

        String uri = baseURL + "/" + paymentId;

        try {
            return this.getRestTemplate().getForObject(uri, CieloResponseDTO.class);
        } catch (RestClientException restClientException) {
            logger.error("Exception calling Cielo API. Message: {}", restClientException.getMessage());
            if (restClientException instanceof HttpClientErrorException.NotFound) {
                logger.error("Unable to find transaction with given Payment Id");
            } else if (restClientException instanceof HttpClientErrorException.Unauthorized) {
                logger.error("The acess to Cielo service was not granted");
            }
            return null;
        } catch (Exception other) {
            logger.error("Unhandled error with message: {}", other.getMessage());
            logger.error(String.valueOf(other));
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
                .build();
    }
}
