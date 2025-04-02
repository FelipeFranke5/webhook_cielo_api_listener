package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error handler error")
public class ErrorHandlerException extends RuntimeException {
    public ErrorHandlerException(String message) {
        super(message);
    }
}
