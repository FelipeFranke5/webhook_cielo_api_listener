package dev.franke.felipe.transaction_manager.api.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.franke.felipe.transaction_manager.api.model.ErrorToSaveModel;
import dev.franke.felipe.transaction_manager.api.service.error_handler.ErrorToSaveService;
import java.util.List;
import org.jeasy.random.EasyRandom;
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
class ErrorToSaveControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorToSaveService errorService;

    @Test
    @DisplayName("GET /api/errors - Should Return List of Errors")
    void getAll_ShouldReturnListOfErrors() throws Exception {
        // Arrange
        EasyRandom generator = new EasyRandom();
        List<ErrorToSaveModel> errorList = List.of(generator.nextObject(ErrorToSaveModel.class));
        when(errorService.listEverything()).thenReturn(errorList);

        // Act + Assert
        mockMvc.perform(get("/api/errors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(errorService).listEverything();
    }

    @Test
    @DisplayName("GET /api/errors/{paymentId} - Valid Payment ID - Should Return List of Errors")
    void getAllByPayment_ValidPaymentId_ShouldReturnListOfErrors() throws Exception {
        // Arrange
        EasyRandom generator = new EasyRandom();
        String paymentId = generator.nextObject(String.class);
        List<ErrorToSaveModel> errorList = List.of(generator.nextObject(ErrorToSaveModel.class));
        when(errorService.listByPaymentId(paymentId)).thenReturn(errorList);

        // Act + Assert
        mockMvc.perform(get("/api/errors/" + paymentId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(errorService).listByPaymentId(paymentId);
    }
}
