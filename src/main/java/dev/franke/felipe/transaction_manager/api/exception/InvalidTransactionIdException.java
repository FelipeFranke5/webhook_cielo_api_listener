package dev.franke.felipe.transaction_manager.api.exception;

public class InvalidTransactionIdException extends RuntimeException {
    public InvalidTransactionIdException(String message) {
        super(message);
    }
}
