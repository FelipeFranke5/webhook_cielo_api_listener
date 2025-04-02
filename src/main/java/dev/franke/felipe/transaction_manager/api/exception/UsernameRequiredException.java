package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username is required and cannot be blank")
public class UsernameRequiredException extends RuntimeException {
    public UsernameRequiredException() {
        super();
    }
}
