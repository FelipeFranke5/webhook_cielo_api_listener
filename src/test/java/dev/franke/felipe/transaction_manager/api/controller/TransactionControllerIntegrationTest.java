package dev.franke.felipe.transaction_manager.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.franke.felipe.transaction_manager.api.dto.TransactionDTO;
import dev.franke.felipe.transaction_manager.api.dto.TransactionResponseDTO;
import dev.franke.felipe.transaction_manager.api.service.TransactionService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    @DisplayName("GET /api/transactions - Should Return List of Transactions")
    void getTransactions_ShouldReturnList() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        TransactionDTO data = generateData(id.toString());
        TransactionDTO data1 = generateData(id2.toString());
        TransactionResponseDTO responseDTO1 = new TransactionResponseDTO(data, OffsetDateTime.now(), id);
        TransactionResponseDTO responseDTO2 = new TransactionResponseDTO(data1, OffsetDateTime.now(), id2);
        List<TransactionResponseDTO> transactionList = List.of(responseDTO1, responseDTO2);
        when(transactionService.getListOfTransactions()).thenReturn(transactionList);

        // Act + Assert
        mockMvc.perform(get("/api/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService).getListOfTransactions();
    }

    @Test
    @DisplayName("GET /api/transactions/{id} - Valid ID - Should Return Transaction")
    void getTransactionById_ValidId_ShouldReturnTransaction() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        String stringId = id.toString();
        TransactionDTO data = generateData(stringId);
        TransactionResponseDTO transaction = new TransactionResponseDTO(data, OffsetDateTime.now(), id);
        when(transactionService.getTransactionById(stringId)).thenReturn(transaction);

        // Act + Assert
        mockMvc.perform(get("/api/transactions/" + stringId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService).getTransactionById(stringId);
    }

    @Test
    @DisplayName("GET /api/transactions/{paymentId}/list - Valid Payment ID - Should Return List of Transactions")
    void getTransactionsByPayment_ValidPaymentId_ShouldReturnList() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        String stringId = id.toString();
        TransactionDTO data = generateData(stringId);
        TransactionResponseDTO transaction = new TransactionResponseDTO(data, OffsetDateTime.now(), id);
        List<TransactionResponseDTO> transactionList = List.of(transaction);
        when(transactionService.getListOfTransactionsByPayment(stringId)).thenReturn(transactionList);

        // Act + Assert
        mockMvc.perform(get("/api/transactions/" + stringId + "/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService).getListOfTransactionsByPayment(stringId);
    }

    TransactionDTO generateData(String paymentId) {
        return new TransactionDTO("A".repeat(5), "1".repeat(19), "CreditCard", paymentId, 1);
    }
}
