package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.TransactionRequestDTO;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

    private static final Logger logger = LoggerFactory.getLogger(ListenerService.class);

    @Autowired
    private CieloQueryService cieloQueryService;

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(id = "webhooklistener1", topics = "webhook-cielo")
    public CompletableFuture<Void> consume(String paymentId) {
        logger.info("Received message with PaymentId: {}", paymentId);
        return CompletableFuture.supplyAsync(() -> cieloQueryService.getTransaction(paymentId))
                .thenAccept(cieloResponseDTO -> {
                    if (cieloResponseDTO != null) {
                        logger.info("API Call to Cielo was successful. Attemping to save returned data");
                        TransactionRequestDTO requestDTO = transactionService.transferToRequest(cieloResponseDTO);
                        transactionService.saveTransactionToDB(requestDTO);
                    }
                });
    }
}
