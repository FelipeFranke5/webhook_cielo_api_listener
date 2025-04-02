package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with this email or username already exists")
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super();
    }
}
