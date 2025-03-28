package dev.franke.felipe.transaction_manager.api.repository;

import dev.franke.felipe.transaction_manager.api.model.ErrorToSaveModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorToSaveRepository extends JpaRepository<ErrorToSaveModel, Long> {
    List<ErrorToSaveModel> findByPaymentId(String paymentId);
}
