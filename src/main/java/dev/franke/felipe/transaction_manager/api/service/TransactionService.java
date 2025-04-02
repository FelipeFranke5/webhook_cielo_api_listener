package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.TransactionRequestDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.InvalidTransactionIdException;
import dev.franke.felipe.transaction_manager.api.exception.TransactionNotFoundException;
import dev.franke.felipe.transaction_manager.api.model.TransactionModel;
import dev.franke.felipe.transaction_manager.api.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    public TransactionService(final TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionModel> getListOfTransactions() {
        return transactionRepository.findAll();
    }

    public List<TransactionModel> getListOfTransactionsByPayment(final String paymentId) {
        this.validateId("invalid payment id format", paymentId);
        return transactionRepository.findByPaymentId(paymentId);
    }

    public TransactionModel getTransactionById(final String id) {
        Assert.notNull(id, "id cannot be null");
        logger.info("RETRIEVING DATA");
        logger.info("ID = {}", id);
        UUID transactionId = null;
        this.validateId("invalid id format", id);
        transactionId = UUID.fromString(id);
        var exception = new TransactionNotFoundException(null);
        final var transaction = transactionRepository.findById(transactionId).orElseThrow(() -> exception);
        logger.info("RETRIEVED ..");
        return transaction;
    }

    TransactionRequestDTO transferToRequest(final CieloResponseDTO cieloResponseDTO) {
        return new TransactionRequestDTO(
                cieloResponseDTO.orderId(),
                cieloResponseDTO.payment().tid(),
                cieloResponseDTO.payment().type(),
                cieloResponseDTO.payment().paymentId(),
                cieloResponseDTO.payment().status());
    }

    void saveTransactionToDB(final TransactionRequestDTO request) {
        logger.info("Initializing save method");
        final var builder = new TransactionModel.TransactionModelBuilder();
        final var transaction = builder.orderId(request.orderId())
                .acquirerTransactionId(request.acquirerTransactionId())
                .paymentType(request.paymentType())
                .paymentId(request.paymentId())
                .paymentStatus(request.paymentStatus())
                .build();
        this.save(transaction);
    }

    private void validateId(final String message, final String id) {
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(message, "message cannot be null");

        try {
            UUID.fromString(id);
        } catch (final IllegalArgumentException exception) {
            throw new InvalidTransactionIdException(message);
        }
    }

    private void save(final TransactionModel transaction) {
        Assert.notNull(transaction, "transaction instance cannot be null");

        try {
            transactionRepository.save(transaction);
            logger.info("Successfully saved transaction");
        } catch (final Exception exception) {
            logger.error("Failed to save transaction with message: {}", exception.getMessage());
            logger.error(String.valueOf(exception));
        }
    }
}
