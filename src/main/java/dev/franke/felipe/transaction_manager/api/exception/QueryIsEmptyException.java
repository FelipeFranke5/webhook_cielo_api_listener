package dev.franke.felipe.transaction_manager.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "List is Empty")
public class QueryIsEmptyException extends RuntimeException {
    public QueryIsEmptyException(String message) {
        super(message);
    }
}
