package dev.franke.felipe.transaction_manager.api.service;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService {

    private static final Logger logger = LoggerFactory.getLogger(ListenerService.class);

    private final CieloQueryService cieloQueryService;
    private final TransactionService transactionService;

    public ListenerService(CieloQueryService cieloQueryService, TransactionService transactionService) {
        this.cieloQueryService = cieloQueryService;
        this.transactionService = transactionService;
    }

    @KafkaListener(id = "webhooklistener1", topics = "webhook-cielo")
    public CompletableFuture<Void> consume(final String paymentId) {
        logger.info("Received message with PaymentId: {}", paymentId);
        return CompletableFuture.supplyAsync(() -> cieloQueryService.getTransaction(paymentId, false))
                .thenAccept(cieloResponseDTO -> {
                    if (cieloResponseDTO != null) {
                        logger.info("API Call to Cielo was successful. Attemping to save returned data");
                        final var requestDTO = transactionService.transferToRequest(cieloResponseDTO);
                        transactionService.saveTransactionToDB(requestDTO);
                    }
                });
    }
}
