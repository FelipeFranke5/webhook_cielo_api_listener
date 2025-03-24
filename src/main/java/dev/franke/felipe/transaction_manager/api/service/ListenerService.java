package dev.franke.felipe.transaction_manager.api.service;

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
    public void consume(String paymentId) {
        logger.info("\n\n- - - - - - - - ABOUT TO ENTER AYSNC NESTED FUNCTIONS");

        cieloQueryService.runTransactionTask(paymentId).thenAccept((cieloResponseDTO -> {
            if (cieloResponseDTO != null) {
                logger.info("\n\n- - - - - - - - cieloResponse not null, attempting to save do db");
                transactionService.runSaveTask(cieloResponseDTO);
            } else {
                logger.error("\n\n- - - - - - - - cieloResponse is null");
            }
        }));
    }
}
