package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.TransactionDTO;
import dev.franke.felipe.transaction_manager.api.dto.TransactionRequestDTO;
import dev.franke.felipe.transaction_manager.api.dto.TransactionResponseDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.InvalidTransactionIdException;
import dev.franke.felipe.transaction_manager.api.exception.TransactionNotFoundException;
import dev.franke.felipe.transaction_manager.api.exception.TransactionServiceException;
import dev.franke.felipe.transaction_manager.api.model.TransactionModel;
import dev.franke.felipe.transaction_manager.api.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponseDTO> getListOfTransactions() {
        var transactions = transactionRepository.findAll();
        return streamToList(transactions);
    }

    public List<TransactionResponseDTO> getListOfTransactionsByPayment(String paymentId) {
        try {
            UUID.fromString(paymentId);
            var results = transactionRepository.findByPaymentId(paymentId);
            return streamToList(results);
        } catch (IllegalArgumentException exception) {
            throw new TransactionServiceException("invalid payment id format");
        }
    }

    public TransactionResponseDTO getTransactionById(String id) {
        logger.info("RETRIEVING DATA");
        logger.info("ID = {}", id);
        UUID transactionId;

        try {
            transactionId = UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new InvalidTransactionIdException(exception.getMessage());
        }

        var transaction =
                transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(null));

        logger.info("RETRIEVED ..");

        var data = new TransactionDTO(
                transaction.getOrderId(),
                transaction.getAcquirerTransactionId(),
                transaction.getPaymentType(),
                transaction.getPaymentId(),
                transaction.getPaymentStatus());
        return new TransactionResponseDTO(data, transaction.getCreatedAt(), transaction.getId());
    }

    TransactionRequestDTO transferToRequest(CieloResponseDTO cieloResponseDTO) {
        return new TransactionRequestDTO(
                cieloResponseDTO.orderId(),
                cieloResponseDTO.payment().tid(),
                cieloResponseDTO.payment().type(),
                cieloResponseDTO.payment().paymentId(),
                cieloResponseDTO.payment().status());
    }

    void saveTransactionToDB(TransactionRequestDTO request) {
        logger.info("Initializing save method");
        TransactionModel.TransactionModelBuilder builder = new TransactionModel.TransactionModelBuilder();

        TransactionModel transaction = builder.orderId(request.orderId())
                .acquirerTransactionId(request.acquirerTransactionId())
                .paymentType(request.paymentType())
                .paymentId(request.paymentId())
                .paymentStatus(request.paymentStatus())
                .build();

        try {
            transactionRepository.save(transaction);
            logger.info("Successfully saved transaction");
        } catch (Exception exception) {
            logger.error("Failed to save transaction with message: {}", exception.getMessage());
            logger.trace(String.valueOf(exception));
        }
    }

    List<TransactionResponseDTO> streamToList(List<TransactionModel> list) {
        return list.stream()
                .map(transactionModel -> new TransactionResponseDTO(
                        new TransactionDTO(
                                transactionModel.getOrderId(),
                                transactionModel.getAcquirerTransactionId(),
                                transactionModel.getPaymentType(),
                                transactionModel.getPaymentId(),
                                transactionModel.getPaymentStatus()),
                        transactionModel.getCreatedAt(),
                        transactionModel.getId()))
                .toList();
    }
}
