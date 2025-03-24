package dev.franke.felipe.transaction_manager.api.repository;

import dev.franke.felipe.transaction_manager.api.model.TransactionModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {}
