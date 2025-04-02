package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Username should have at least 1 special character and 1 number")
public class UsernameHasInvalidRegexException extends RuntimeException {
    public UsernameHasInvalidRegexException() {
        super();
    }
}
