package dev.franke.felipe.transaction_manager.api.exception;

public class TransactionServiceException extends RuntimeException {
    public TransactionServiceException(String message) {
        super(message);
    }
}
