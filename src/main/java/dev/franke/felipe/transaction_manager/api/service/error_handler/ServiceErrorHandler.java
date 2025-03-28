package dev.franke.felipe.transaction_manager.api.service.error_handler;

import dev.franke.felipe.transaction_manager.api.exception.ErrorHandlerException;
import java.util.UUID;

public class ServiceErrorHandler {

    private String message;
    private String paymentId;

    public ServiceErrorHandler(String message, String paymentId) {
        this.setMessage(message);
        this.setPaymentId(paymentId);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void validate() {

        if (this.getMessage() == null || this.getPaymentId() == null) {
            throw new ErrorHandlerException("Message or PaymentId cannot be null");
        }

        if (this.getMessage().isBlank() || this.getPaymentId().isBlank()) {
            throw new ErrorHandlerException("Message or PaymentId cannot be blank");
        }

        if (this.getMessage().length() < 5 || this.getMessage().length() > 254) {
            throw new ErrorHandlerException("Message length not accepted. Min length is 5 and max is 254");
        }

        try {
            UUID.fromString(paymentId);
        } catch (IllegalArgumentException exception) {
            throw new ErrorHandlerException("PaymentId should be in UUID format");
        }
    }
}
