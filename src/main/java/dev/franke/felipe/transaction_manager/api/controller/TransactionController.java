package dev.franke.felipe.transaction_manager.api.controller;

import dev.franke.felipe.transaction_manager.api.dto.TransactionResponseDTO;
import dev.franke.felipe.transaction_manager.api.service.TransactionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<TransactionResponseDTO> transactions() {
        return transactionService.getListOfTransactions();
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO transaction(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/{paymentId}/list")
    public List<TransactionResponseDTO> transactionsByPayment(@PathVariable String paymentId) {
        return transactionService.getListOfTransactionsByPayment(paymentId);
    }
}
