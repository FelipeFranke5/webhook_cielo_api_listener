package dev.franke.felipe.transaction_manager.api.service.error_handler;

import dev.franke.felipe.transaction_manager.api.exception.ErrorToSaveServiceException;
import dev.franke.felipe.transaction_manager.api.exception.QueryIsEmptyException;
import dev.franke.felipe.transaction_manager.api.model.ErrorToSaveModel;
import dev.franke.felipe.transaction_manager.api.repository.ErrorToSaveRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ErrorToSaveService {

    private final ErrorToSaveRepository errorRepo;

    public ErrorToSaveService(final ErrorToSaveRepository errorRepo) {
        this.errorRepo = errorRepo;
    }

    public List<ErrorToSaveModel> listEverything() {
        return errorRepo.findAll();
    }

    public List<ErrorToSaveModel> listByPaymentId(final String paymentId) {
        validatePaymentId(paymentId);
        return errorRepo.findByPaymentId(paymentId);
    }

    public void persist(final ServiceErrorHandler handler) {
        handler.validate();
        final var model = new ErrorToSaveModel(handler.getPaymentId(), handler.getMessage());
        errorRepo.save(model);
    }

    public void deleteEverything() {
        errorRepo.deleteAll();
    }

    public void deleteEverythingByPaymentId(final String paymentId) {
        validatePaymentId(paymentId);
        final var models = errorRepo.findByPaymentId(paymentId);
        if (models.isEmpty()) throw new QueryIsEmptyException("no results for query");
        models.stream().forEach(errorRepo::delete);
    }

    private void validatePaymentId(final String paymentId) {
        try {
            UUID.fromString(paymentId);
        } catch (final IllegalArgumentException exception) {
            throw new ErrorToSaveServiceException("invalid payment id format");
        }
    }
}
