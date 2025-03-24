package dev.franke.felipe.transaction_manager.api.controller;

import dev.franke.felipe.transaction_manager.api.dto.TransactionDTO;
import dev.franke.felipe.transaction_manager.api.service.TransactionService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionDTO> transactions() {
        return transactionService.getListOfTransactions();
    }

    @GetMapping("/{id}")
    public TransactionDTO transaction(@PathVariable String id) {
        return transactionService.getTransactionById(id);
    }
}
