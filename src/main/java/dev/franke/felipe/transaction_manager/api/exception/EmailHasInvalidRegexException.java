package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Not a valid Email address")
public class EmailHasInvalidRegexException extends RuntimeException {
    public EmailHasInvalidRegexException() {
        super();
    }
}
