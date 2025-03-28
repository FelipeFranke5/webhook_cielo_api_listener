package dev.franke.felipe.transaction_manager.api.service.error_handler;

import dev.franke.felipe.transaction_manager.api.exception.ErrorToSaveServiceException;
import dev.franke.felipe.transaction_manager.api.model.ErrorToSaveModel;
import dev.franke.felipe.transaction_manager.api.repository.ErrorToSaveRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorToSaveService {

    @Autowired
    private ErrorToSaveRepository errorRepo;

    public List<ErrorToSaveModel> listEverything() {
        return errorRepo.findAll();
    }

    public List<ErrorToSaveModel> listByPaymentId(String paymentId) {
        try {
            UUID.fromString(paymentId);
            return errorRepo.findByPaymentId(paymentId);
        } catch (IllegalArgumentException exception) {
            throw new ErrorToSaveServiceException("invalid payment id format");
        }
    }

    public void persist(ServiceErrorHandler handler) {
        ErrorToSaveModel model = new ErrorToSaveModel(handler.getPaymentId(), handler.getMessage());
        errorRepo.save(model);
    }
}
