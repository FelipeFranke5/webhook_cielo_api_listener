package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Password is required and cannot be blank")
public class PasswordRequiredException extends RuntimeException {
    public PasswordRequiredException() {
        super();
    }
}
