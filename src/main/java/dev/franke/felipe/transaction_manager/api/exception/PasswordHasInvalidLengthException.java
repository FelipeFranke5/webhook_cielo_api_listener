package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Password should have at least 8 characters and should not exceed 30 characters")
public class PasswordHasInvalidLengthException extends RuntimeException {
    public PasswordHasInvalidLengthException() {
        super();
    }
}
