package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error to Save")
public class ErrorToSaveServiceException extends RuntimeException {
    public ErrorToSaveServiceException(String message) {
        super(message);
    }
}
