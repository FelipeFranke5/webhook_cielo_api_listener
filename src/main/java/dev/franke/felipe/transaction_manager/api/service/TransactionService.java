package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.TransactionDTO;
import dev.franke.felipe.transaction_manager.api.dto.TransactionRequestDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.InvalidTransactionIdException;
import dev.franke.felipe.transaction_manager.api.exception.TransactionNotFoundException;
import dev.franke.felipe.transaction_manager.api.model.TransactionModel;
import dev.franke.felipe.transaction_manager.api.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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

    public List<TransactionDTO> getListOfTransactions() {
        List<TransactionModel> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map((transactionModel -> new TransactionDTO(
                        transactionModel.getId(),
                        transactionModel.getOrderId(),
                        transactionModel.getAcquirerTransactionId(),
                        transactionModel.getPaymentType(),
                        transactionModel.getPaymentId(),
                        transactionModel.getPaymentStatus())))
                .toList();
    }

    public TransactionDTO getTransactionById(String id) {
        logger.info("\n\n- - - - - - - - RETRIEVING DATA");
        logger.info("- - - - - - - - ID = {}", id);
        UUID transactionId;

        try {
            transactionId = UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new InvalidTransactionIdException(exception.getMessage());
        }

        TransactionModel transaction =
                transactionRepository.findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(null));

        logger.info("- - - - - - - - RETRIEVED ..");

        return new TransactionDTO(
                transactionId,
                transaction.getOrderId(),
                transaction.getAcquirerTransactionId(),
                transaction.getPaymentType(),
                transaction.getPaymentId(),
                transaction.getPaymentStatus());
    }

    void runSaveTask(CieloResponseDTO cieloResponseDTO) {
        CompletableFuture.runAsync(() -> saveTransactionToDB(transferToRequest(cieloResponseDTO)));
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
        logger.info("\n\n- - - - - - - - RECEIVED CALL TO SAVE TRANSACTION");

        TransactionModel.TransactionModelBuilder builder = new TransactionModel.TransactionModelBuilder();
        TransactionModel transaction = builder.orderId(request.orderId())
                .acquirerTransactionId(request.acquirerTransactionId())
                .paymentType(request.paymentType())
                .paymentStatus(request.paymentStatus())
                .build();

        logger.info("- - - - - - - - AFTER BUILDING THE TRANSACTION, WE ARE ABOUT TO ATTEMPT TO SAVE ..");

        try {
            transactionRepository.save(transaction);
            logger.info("SAVED WITH SUCESS");
        } catch (Exception exception) {
            logger.error("UNABLE TO SAVE DUE TO AN ERROR: {}", exception.getMessage());
            logger.trace(String.valueOf(exception));
        } finally {
            logger.info("- - - - - - - - POST SAVE ..");
        }
    }
}
