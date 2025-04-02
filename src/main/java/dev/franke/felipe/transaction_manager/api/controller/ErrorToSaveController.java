package dev.franke.felipe.transaction_manager.api.controller;

import dev.franke.felipe.transaction_manager.api.model.ErrorToSaveModel;
import dev.franke.felipe.transaction_manager.api.service.error_handler.ErrorToSaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/errors")
@SecurityRequirement(name = "basicAuth")
@Tag(name = "ErrorToSave", description = "Stores all messages that could not be saved")
public class ErrorToSaveController {

    private final ErrorToSaveService errorService;

    public ErrorToSaveController(ErrorToSaveService errorToSaveService) {
        this.errorService = errorToSaveService;
    }

    @GetMapping
    @Operation(summary = "Get all errors", description = "Method for retrieving errors that were stored in DB")
    @ApiResponse(responseCode = "200", description = "Data retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public ResponseEntity<List<ErrorToSaveModel>> getAll() {
        return ResponseEntity.ok(errorService.listEverything());
    }

    @GetMapping("{paymentId}")
    @Operation(
            summary = "Get all errors by Payment ID",
            description = "Method for retrieving errors that were stored in DB with the same PaymentId")
    @ApiResponse(responseCode = "200", description = "Data retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid ID")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public ResponseEntity<List<ErrorToSaveModel>> getAllByPayment(@PathVariable final String paymentId) {
        return ResponseEntity.ok(errorService.listByPaymentId(paymentId));
    }

    @DeleteMapping
    @Operation(summary = "Delete all errors", description = "Will clear all error records")
    @ApiResponse(responseCode = "204", description = "Sucessfully deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public ResponseEntity<Void> deleteAll() {
        errorService.deleteEverything();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{paymentId}")
    @Operation(
            summary = "Delete all errors by PaymentId",
            description = "Will clear all error records with a given PaymentId")
    @ApiResponse(responseCode = "204", description = "Sucessfully deleted")
    @ApiResponse(responseCode = "400", description = "Invalid ID")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "No results for the query")
    @ApiResponse(responseCode = "500", description = "Internal error")
    public ResponseEntity<Void> deleteOne(@PathVariable final String paymentId) {
        errorService.deleteEverythingByPaymentId(paymentId);
        return ResponseEntity.noContent().build();
    }
}
