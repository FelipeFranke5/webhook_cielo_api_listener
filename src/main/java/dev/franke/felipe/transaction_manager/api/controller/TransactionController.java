package dev.franke.felipe.transaction_manager.api.controller;

import dev.franke.felipe.transaction_manager.api.model.TransactionModel;
import dev.franke.felipe.transaction_manager.api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "Transactions", description = "Stores all messages that could be retrieved from Cielo API and were saved")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Get the list of saved transactions")
    @ApiResponse(responseCode = "200", description = "Data retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public List<TransactionModel> transactions() {
        return transactionService.getListOfTransactions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by Id", description = "Get specific Transaction using its ID")
    @ApiResponse(responseCode = "200", description = "Data retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid ID")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public TransactionModel transaction(@PathVariable final String id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/{paymentId}/list")
    @Operation(
            summary = "Get all transactions by paymentId",
            description = "Get a list of transactions with same paymentId")
    @ApiResponse(responseCode = "200", description = "Data retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid ID")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public List<TransactionModel> transactionsByPayment(@PathVariable final String paymentId) {
        return transactionService.getListOfTransactionsByPayment(paymentId);
    }
}
