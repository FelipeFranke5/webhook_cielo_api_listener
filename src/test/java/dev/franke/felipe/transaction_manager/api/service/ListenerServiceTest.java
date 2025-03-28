package dev.franke.felipe.transaction_manager.api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.franke.felipe.transaction_manager.api.dto.TransactionRequestDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponsePaymentDTO;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListenerServiceTest {

    @Mock
    private CieloQueryService cieloQueryService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private ListenerService listenerService;

    private final String paymentId = UUID.randomUUID().toString();

    @BeforeEach
    void setUpBeforeEach() {
        CieloResponseDTO cieloResponseMock = createCieloResponseDTO();
        lenient().when(cieloQueryService.getTransaction(paymentId, false)).thenReturn(cieloResponseMock);
    }

    @Test
    @DisplayName("Method 'consume' - Valid Data - Should Execute Normally")
    void consume_ValidData_ShouldExecuteTaskNormally() {
        // Arrange
        CieloResponseDTO cieloResponseMock = createCieloResponseDTO();
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                cieloResponseMock.orderId(),
                cieloResponseMock.payment().tid(),
                cieloResponseMock.payment().type(),
                cieloResponseMock.payment().paymentId(),
                cieloResponseMock.payment().status());

        when(cieloQueryService.getTransaction(paymentId, false)).thenReturn(cieloResponseMock);
        when(transactionService.transferToRequest(cieloResponseMock)).thenReturn(requestDTO);

        // Act
        CompletableFuture<Void> future = listenerService.consume(paymentId);
        future.join();

        // Assert
        verify(cieloQueryService, times(1)).getTransaction(paymentId, false);
        verify(transactionService, times(1)).transferToRequest(cieloResponseMock);
        verify(transactionService, times(1)).saveTransactionToDB(requestDTO);
    }

    @Test
    @DisplayName("Method 'consume' - Receives null response - Should Never Call saveTransactionToDB")
    void consume_NullResponse_ShouldNeverCallSaveTransactionToDB() {
        // Arrange
        when(cieloQueryService.getTransaction(paymentId, false)).thenReturn(null);

        // Act
        CompletableFuture<Void> future = listenerService.consume(paymentId);
        future.join();

        // Assert
        verify(cieloQueryService, times(1)).getTransaction(paymentId, false);
        verify(transactionService, never()).transferToRequest(any());
        verify(transactionService, never()).saveTransactionToDB(any());
    }

    CieloResponseDTO createCieloResponseDTO() {
        return new CieloResponseDTO(
                "1234",
                new CieloResponsePaymentDTO(
                        "11111111111111111111", "CreditCard", UUID.randomUUID().toString(), 1));
    }
}
