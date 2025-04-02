package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid TransactionId")
public class InvalidTransactionIdException extends RuntimeException {
    public InvalidTransactionIdException(String message) {
        super(message);
    }
}
