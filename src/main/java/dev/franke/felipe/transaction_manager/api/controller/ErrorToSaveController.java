package dev.franke.felipe.transaction_manager.api.controller;

import dev.franke.felipe.transaction_manager.api.model.ErrorToSaveModel;
import dev.franke.felipe.transaction_manager.api.service.error_handler.ErrorToSaveService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/errors")
public class ErrorToSaveController {

    @Autowired
    private ErrorToSaveService errorService;

    @GetMapping
    public List<ErrorToSaveModel> getAll() {
        return errorService.listEverything();
    }

    @GetMapping("{paymentId}")
    public List<ErrorToSaveModel> getAllByPayment(@PathVariable String paymentId) {
        return errorService.listByPaymentId(paymentId);
    }
}
