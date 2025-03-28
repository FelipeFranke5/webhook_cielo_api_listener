package dev.franke.felipe.transaction_manager.api.service.error_handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.franke.felipe.transaction_manager.api.exception.ErrorHandlerException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ServiceErrorHandlerTest {

    @Test
    @DisplayName("Validate - Valid Data - Should Not Throw Exception")
    void validate_ValidData_ShouldNotThrowException() {
        // Arrange
        String validMessage = "This is a valid error message";
        String validPaymentId = UUID.randomUUID().toString();
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(validMessage, validPaymentId);

        // Act + Assert
        assertDoesNotThrow(serviceErrorHandler::validate);
    }

    @Test
    @DisplayName("Validate - Null Message - Should Throw ErrorHandlerException")
    void validate_NullMessage_ShouldThrowException() {
        // Arrange
        String validPaymentId = UUID.randomUUID().toString();
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(null, validPaymentId);

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("Message or PaymentId cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Validate - Null PaymentId - Should Throw ErrorHandlerException")
    void validate_NullPaymentId_ShouldThrowException() {
        // Arrange
        String validMessage = "This is a valid error message";
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(validMessage, null);

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("Message or PaymentId cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Validate - Blank Message - Should Throw ErrorHandlerException")
    void validate_BlankMessage_ShouldThrowException() {
        // Arrange
        String validPaymentId = UUID.randomUUID().toString();
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(" ", validPaymentId);

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("Message or PaymentId cannot be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Validate - Blank PaymentId - Should Throw ErrorHandlerException")
    void validate_BlankPaymentId_ShouldThrowException() {
        // Arrange
        String validMessage = "This is a valid error message";
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(validMessage, " ");

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("Message or PaymentId cannot be blank", exception.getMessage());
    }

    @Test
    @DisplayName("Validate - Message Length Below Min - Should Throw ErrorHandlerException")
    void validate_MessageTooShort_ShouldThrowException() {
        // Arrange
        String validPaymentId = UUID.randomUUID().toString();
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler("Err", validPaymentId);

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("Message length not accepted. Min length is 5 and max is 254", exception.getMessage());
    }

    @Test
    @DisplayName("Validate - Message Length Above Max - Should Throw ErrorHandlerException")
    void validate_MessageTooLong_ShouldThrowException() {
        // Arrange
        String validPaymentId = UUID.randomUUID().toString();
        String longMessage = "A".repeat(255);
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(longMessage, validPaymentId);

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("Message length not accepted. Min length is 5 and max is 254", exception.getMessage());
    }

    @Test
    @DisplayName("Validate - Invalid PaymentId Format - Should Throw ErrorHandlerException")
    void validate_InvalidPaymentId_ShouldThrowException() {
        // Arrange
        String validMessage = "This is a valid error message";
        String invalidPaymentId = "12345";
        ServiceErrorHandler serviceErrorHandler = new ServiceErrorHandler(validMessage, invalidPaymentId);

        // Act + Assert
        ErrorHandlerException exception = assertThrows(ErrorHandlerException.class, serviceErrorHandler::validate);
        assertEquals("PaymentId should be in UUID format", exception.getMessage());
    }
}
