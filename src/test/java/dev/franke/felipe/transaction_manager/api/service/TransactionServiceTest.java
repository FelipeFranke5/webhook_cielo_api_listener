package dev.franke.felipe.transaction_manager.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.franke.felipe.transaction_manager.api.dto.TransactionRequestDTO;
import dev.franke.felipe.transaction_manager.api.exception.InvalidTransactionIdException;
import dev.franke.felipe.transaction_manager.api.exception.TransactionNotFoundException;
import dev.franke.felipe.transaction_manager.api.model.TransactionModel;
import dev.franke.felipe.transaction_manager.api.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Method 'getListOfTransactions' - No transactions - Should Return empty list")
    void getListOfTRansactions_NoTransactions_ShouldReturnEmptyList() {
        // Arrange
        List<TransactionModel> dabataseReturn = List.of();
        when(transactionRepository.findAll()).thenReturn(dabataseReturn);
        // Act
        var actual = transactionService.getListOfTransactions();
        // Assert
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Method 'getListOfTransactions' - 4 transactions - Should Return filled list")
    void getListOfTransactions_4Transactions_ShouldReturnList() {
        // Arrange
        EasyRandom generator = new EasyRandom();
        TransactionModel transaction1 = generator.nextObject(TransactionModel.class);
        TransactionModel transaction2 = generator.nextObject(TransactionModel.class);
        TransactionModel transaction3 = generator.nextObject(TransactionModel.class);
        TransactionModel transaction4 = generator.nextObject(TransactionModel.class);
        List<TransactionModel> dabataseReturn = List.of(transaction1, transaction2, transaction3, transaction4);
        when(transactionRepository.findAll()).thenReturn(dabataseReturn);
        // Act
        var actual = transactionService.getListOfTransactions();
        // Assert
        assertNotNull(actual);
        assertEquals(4, actual.size());
        assertEquals(actual.get(0).data().orderId(), transaction1.getOrderId());
        assertEquals(actual.get(1).data().orderId(), transaction2.getOrderId());
        assertEquals(actual.get(2).data().orderId(), transaction3.getOrderId());
        assertEquals(actual.get(3).data().orderId(), transaction4.getOrderId());
    }

    @Test
    @DisplayName("Method 'getTransactionById' - Valid ID - Should Return TransactionDTO")
    void getTransactionById_ValidId_ShouldReturnTransactionDTO() {
        // Arrange
        EasyRandom generator = new EasyRandom();
        TransactionModel transaction1 = generator.nextObject(TransactionModel.class);
        when(transactionRepository.findById(any(UUID.class))).thenReturn(Optional.of(transaction1));
        // Act
        var actual = transactionService.getTransactionById(transaction1.getId().toString());
        // Assert
        assertNotNull(actual);
        assertEquals(transaction1.getOrderId(), actual.data().orderId());
        assertEquals(transaction1.getAcquirerTransactionId(), actual.data().acquirerTransactionId());
        assertEquals(transaction1.getPaymentId(), actual.data().paymentId());
    }

    @Test
    @DisplayName("Method 'getTransactionById' - Invalid ID - Should Throw TransactionNotFoundException")
    void getTransactionById_InvalidId_ShouldThrow() {
        // Arrange
        when(transactionRepository.findById(any(UUID.class))).thenThrow(TransactionNotFoundException.class);
        // Act + Assert
        assertThrows(
                TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName("Method 'getTransactionById' - Not Valid UUID - Should Throw InvalidTransactionIdException")
    void getTransactionById_NotUUID_ShouldThrow() {
        // Arrange
        String invalidID = "invalid-id";
        // Act + Assert
        assertThrows(InvalidTransactionIdException.class, () -> transactionService.getTransactionById(invalidID));
    }

    @Test
    @DisplayName("Method 'saveTransactionToDB' - Valid Request - Should save")
    void saveTransactionToDB_ValidRequest_ShouldSave() {
        // Arrange
        EasyRandom generator = new EasyRandom();
        TransactionModel transaction1 = generator.nextObject(TransactionModel.class);
        transaction1.setId(null);
        TransactionRequestDTO request = new TransactionRequestDTO(
                transaction1.getOrderId(),
                transaction1.getAcquirerTransactionId(),
                transaction1.getPaymentType(),
                transaction1.getPaymentId(),
                transaction1.getPaymentStatus());
        when(transactionRepository.save(any(TransactionModel.class))).thenReturn(transaction1);
        // Act
        assertDoesNotThrow(() -> transactionService.saveTransactionToDB(request));
        // Assert
        verify(transactionRepository).save(any(TransactionModel.class));
    }
}
