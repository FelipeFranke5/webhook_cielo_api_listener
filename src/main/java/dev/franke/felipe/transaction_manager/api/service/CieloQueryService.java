package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.CredentialsException;
import dev.franke.felipe.transaction_manager.api.service.error_handler.ErrorToSaveService;
import dev.franke.felipe.transaction_manager.api.service.error_handler.ServiceErrorHandler;
import java.util.ArrayList;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CieloQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CieloQueryService.class);

    private final ErrorToSaveService errorService;

    public CieloQueryService(ErrorToSaveService errorToSaveService) {
        this.errorService = errorToSaveService;
    }

    @Value("${cielo.api.url.query}")
    private String baseURL;

    @Value("${cielo.api.merchant.id}")
    private String merchantId;

    @Value("${cielo.api.merchant.key}")
    private String merchantKey;

    private RestTemplate restTemplate;

    void checkCredentials() {
        final var errors = new ArrayList<>();

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

    boolean paymentIdIsValid(final String paymentId) {

        if (paymentId == null || paymentId.isBlank() || paymentId.length() < 36) {
            logger.error("PaymentId is null, blank or invalid");
            logger.info("PaymentId: {}", paymentId);
            return false;
        }

        try {
            UUID.fromString(paymentId);
            return true;
        } catch (final IllegalArgumentException illegalArgumentException) {
            logger.error("PaymentId is not valid UUID");
            logger.info("PaymentId: {}", paymentId);
            return false;
        }
    }

    void saveErrorMessage(final Exception exception, final String paymentId, final boolean test) {
        Assert.notNull(exception, "exception cannot be null here");
        logger.error("Exception calling Cielo API. Message: {}", exception.getMessage());
        String message = "";

        if (exception instanceof HttpClientErrorException.NotFound) {
            message = "PaymentId not found";
        } else if (exception instanceof HttpClientErrorException.Unauthorized) {
            message = "The acess to Cielo service was not granted";
        } else {
            message = exception.getMessage();
        }

        if (!test) {
            logger.error(message);
            var errorHandler = new ServiceErrorHandler(message, paymentId);
            errorHandler.validate();
            errorService.persist(errorHandler);
        }
    }

    CieloResponseDTO getCieloResponseDTO(final String uri, final String paymentId, final boolean test) {
        try {
            return this.getRestTemplate().getForObject(uri, CieloResponseDTO.class);
        } catch (final Exception exception) {
            saveErrorMessage(exception, paymentId, test);
            return null;
        }
    }

    public CieloResponseDTO getTransaction(final String paymentId, final boolean test) {
        logger.info("Initializing method to call Cielo API");

        if (!test) {
            this.setRestTemplate();
        }

        checkCredentials();
        checkURL();

        if (!paymentIdIsValid(paymentId)) {
            return null;
        }

        final var uri = baseURL + "/" + paymentId;
        return getCieloResponseDTO(uri, paymentId, test);
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
        final var builder = new RestTemplateBuilder();
        restTemplate = builder.defaultHeader("MerchantId", this.getMerchantId())
                .defaultHeader("MerchantKey", this.getMerchantKey())
                .build();
    }
}
