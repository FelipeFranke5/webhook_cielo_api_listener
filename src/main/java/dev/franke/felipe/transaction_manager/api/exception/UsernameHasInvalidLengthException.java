package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Username should have at least 5 characters and should not exceed 30 characters")
public class UsernameHasInvalidLengthException extends RuntimeException {
    public UsernameHasInvalidLengthException() {
        super();
    }
}
