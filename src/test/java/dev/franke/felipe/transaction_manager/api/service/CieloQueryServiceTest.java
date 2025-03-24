package dev.franke.felipe.transaction_manager.api.service;

import static java.util.concurrent.TimeUnit.*;
import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponseDTO;
import dev.franke.felipe.transaction_manager.api.dto.cielo_query_response.CieloResponsePaymentDTO;
import dev.franke.felipe.transaction_manager.api.exception.CredentialsException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class CieloQueryServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CieloQueryService cieloQueryService;

    private final String MERCHANT_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUpBeforeEach() {
        String BASE_URL = "https://apiquerysandbox.cieloecommerce.cielo.com.br/1/sales";
        String MERCHANT_KEY = "XKGHUBSBKIRXKAVPSKWLVXYCLVJUGTNZLIHPUSYV";
        ReflectionTestUtils.setField(cieloQueryService, "baseURL", BASE_URL);
        ReflectionTestUtils.setField(cieloQueryService, "merchantId", MERCHANT_ID);
        ReflectionTestUtils.setField(cieloQueryService, "merchantKey", MERCHANT_KEY);
    }

    @Test
    @DisplayName("Method 'getTransaction' - Valid PaymentId - Should Return CieloResponsePaymentDTO")
    void getTransaction_ValidPaymentId_ShouldReturnCieloResponsePaymentDTO() {
        // Arrange
        String paymentId = UUID.randomUUID().toString();
        String uri = cieloQueryService.getBaseURL() + "/" + paymentId;
        CieloResponsePaymentDTO payment = new CieloResponsePaymentDTO(
                "11111111111111111111", "CreditCard", UUID.randomUUID().toString(), 1);
        CieloResponseDTO mockedResponse = new CieloResponseDTO("1234", payment);
        when(restTemplate.getForObject(uri, CieloResponseDTO.class)).thenReturn(mockedResponse);
        // Act
        CieloResponseDTO actual = cieloQueryService.getTransaction(paymentId);
        // Assert
        assertEquals(mockedResponse, actual);
        verify(restTemplate).getForObject(uri, CieloResponseDTO.class);
    }

    @Test
    @DisplayName("Method 'getTransaction' - Valid PaymentId/NotFound - Should Return null")
    void getTransaction_ValidPaymentIdNotFound_ShouldReturnNull() {
        // Arrange
        String paymentId = UUID.randomUUID().toString();
        String uri = cieloQueryService.getBaseURL() + "/" + paymentId;
        CieloResponseDTO mockedResponse = null;
        when(restTemplate.getForObject(uri, CieloResponseDTO.class)).thenThrow(HttpClientErrorException.NotFound.class);
        // Act
        CieloResponseDTO actualResponse = cieloQueryService.getTransaction(paymentId);
        // Assert
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("Method 'getTransaction' - Valid PaymentId/Unauthorized - Should Return null")
    void getTransaction_ValidPaymentIdUnauthorized_ShouldReturnNull() {
        // Arrange
        String paymentId = UUID.randomUUID().toString();
        String uri = cieloQueryService.getBaseURL() + "/" + paymentId;
        CieloResponseDTO mockedResponse = null;
        when(restTemplate.getForObject(uri, CieloResponseDTO.class))
                .thenThrow(HttpClientErrorException.Unauthorized.class);
        // Act
        CieloResponseDTO actualResponse = cieloQueryService.getTransaction(paymentId);
        // Assert
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("Method 'getTransaction' - Valid PaymentId/OtherError - Should Return null")
    void getTransaction_ValidPaymentIdOther_ShouldReturnNull() {
        // Arrange
        String paymentId = UUID.randomUUID().toString();
        String uri = cieloQueryService.getBaseURL() + "/" + paymentId;
        CieloResponseDTO mockedResponse = null;
        when(restTemplate.getForObject(uri, CieloResponseDTO.class)).thenThrow(RestClientException.class);
        // Act
        CieloResponseDTO actualResponse = cieloQueryService.getTransaction(paymentId);
        // Assert
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("Method 'getTransaction' - Invalid PaymentId - Should Return null")
    void getTransaction_InvalidPaymentId_ShouldReturnNull() {
        // Arrange
        String uri = cieloQueryService.getBaseURL() + "/" + null;
        // Act
        CieloResponseDTO actualResponse = cieloQueryService.getTransaction(null);
        // Assert
        assertNull(actualResponse);
        verify(restTemplate, never()).getForObject(uri, CieloResponseDTO.class);
    }

    @Test
    @DisplayName("Method 'runTransactionTask' - All Valid PaymentId - Should Return CompletableFuture")
    void runTransactionTask_ValidPaymentId_ShouldReturnCompletableFuture() {
        // Arrange
        String paymentId1 = UUID.randomUUID().toString();
        String paymentId2 = UUID.randomUUID().toString();

        String uri1 = cieloQueryService.getBaseURL() + "/" + paymentId1;
        String uri2 = cieloQueryService.getBaseURL() + "/" + paymentId2;

        CieloResponsePaymentDTO mockedPayment1 = new CieloResponsePaymentDTO(
                "11111111111111111111", "CreditCard", UUID.randomUUID().toString(), 1);
        CieloResponsePaymentDTO mockedPayment2 = new CieloResponsePaymentDTO(
                "11111111111111111111", "CreditCard", UUID.randomUUID().toString(), 1);

        CieloResponseDTO mockedResponse1 = new CieloResponseDTO("1234", mockedPayment1);
        CieloResponseDTO mockedResponse2 = new CieloResponseDTO("1234", mockedPayment2);

        when(restTemplate.getForObject(uri1, CieloResponseDTO.class)).thenReturn(mockedResponse1);
        when(restTemplate.getForObject(uri2, CieloResponseDTO.class)).thenReturn(mockedResponse2);
        // Act
        CompletableFuture<CieloResponseDTO> futureResponse1 = cieloQueryService.runTransactionTask(paymentId1);
        CompletableFuture<CieloResponseDTO> futureResponse2 = cieloQueryService.runTransactionTask(paymentId1);
        // Assert
        await().atMost(5, SECONDS).until(futureResponse1::isDone);
        await().atMost(5, SECONDS).until(futureResponse2::isDone);
    }

    @Test
    @DisplayName("Method 'runTransactionTask' - Partial Invalid PaymentId - Should Return partial null")
    void runTransactionTask_HalfInvalidPaymentId_ShouldReturnHalfNull()
            throws InterruptedException, ExecutionException {
        // Arrange
        String paymentId1 = UUID.randomUUID().toString();
        String uri1 = cieloQueryService.getBaseURL() + "/" + paymentId1;

        CieloResponsePaymentDTO mockedPayment1 = new CieloResponsePaymentDTO(
                "11111111111111111111", "CreditCard", UUID.randomUUID().toString(), 1);

        CieloResponseDTO mockedResponse1 = new CieloResponseDTO("1234", mockedPayment1);
        when(restTemplate.getForObject(uri1, CieloResponseDTO.class)).thenReturn(mockedResponse1);
        // Act
        CompletableFuture<CieloResponseDTO> futureResponse1 = cieloQueryService.runTransactionTask(paymentId1);
        CompletableFuture<CieloResponseDTO> futureResponse2 = cieloQueryService.runTransactionTask(null);
        // Assert
        await().atMost(5, SECONDS).until(() -> futureResponse1.isDone() && futureResponse2.isDone());
        assertNotNull(futureResponse1.get());
        assertNull(futureResponse2.get());
    }

    @Test
    @DisplayName("Method 'runTransactionTask' - Partial Exception - Should Return partial null")
    void runTransactionTask_HalfException_Should_ShouldReturnHalfNull()
            throws InterruptedException, ExecutionException {
        // Arrange
        String paymentId1 = UUID.randomUUID().toString();
        String paymentId2 = UUID.randomUUID().toString();

        String uri1 = cieloQueryService.getBaseURL() + "/" + paymentId1;
        String uri2 = cieloQueryService.getBaseURL() + "/" + paymentId2;

        CieloResponsePaymentDTO mockedPayment1 = new CieloResponsePaymentDTO(
                "11111111111111111111", "CreditCard", UUID.randomUUID().toString(), 1);

        CieloResponseDTO mockedResponse1 = new CieloResponseDTO("1234", mockedPayment1);
        when(restTemplate.getForObject(uri1, CieloResponseDTO.class)).thenReturn(mockedResponse1);
        when(restTemplate.getForObject(uri2, CieloResponseDTO.class)).thenThrow(RestClientException.class);
        // Act
        CompletableFuture<CieloResponseDTO> futureResponse1 = cieloQueryService.runTransactionTask(paymentId1);
        CompletableFuture<CieloResponseDTO> futureResponse2 = cieloQueryService.runTransactionTask(paymentId2);
        // Assert
        await().atMost(5, SECONDS).until(() -> futureResponse1.isDone() && futureResponse2.isDone());
        assertNotNull(futureResponse1.get());
        assertNull(futureResponse2.get());
    }

    @Test
    @DisplayName("Method 'checkURL' - Invalid URL - Should Throw CredentialsException")
    void checkURL_InvalidURL_ShouldThrow() {
        // Arrange
        ReflectionTestUtils.setField(cieloQueryService, "baseURL", null);
        // Act + Assert
        assertThrows(CredentialsException.class, () -> cieloQueryService.checkURL());
    }

    @Test
    @DisplayName("Method 'checkURL' - Invalid URL2 - Should Throw CredentialsException")
    void checkURL_InvalidURL2_ShouldThrow() {
        // Arrange
        ReflectionTestUtils.setField(cieloQueryService, "baseURL", "");
        // Act + Assert
        assertThrows(CredentialsException.class, () -> cieloQueryService.checkURL());
    }

    @Test
    @DisplayName("Method 'checkURL' - Invalid URL3 - Should Throw CredentialsException")
    void checkURL_InvalidURL3_ShouldThrow() {
        // Arrange
        ReflectionTestUtils.setField(cieloQueryService, "baseURL", "https://www.google.com.br");
        // Act + Assert
        assertThrows(CredentialsException.class, () -> cieloQueryService.checkURL());
    }

    @Test
    @DisplayName("Method 'checkCredentials' - MerchantId or MerchantKey null - Should Throw CredentialsException")
    void checkCredentials_NullMerchantCredentials_ShouldThrowCredentialsException() {
        // Arrange
        ReflectionTestUtils.setField(cieloQueryService, "merchantId", null);
        ReflectionTestUtils.setField(cieloQueryService, "merchantKey", null);
        // Act + Assert
        CredentialsException exception =
                assertThrows(CredentialsException.class, () -> cieloQueryService.checkCredentials());
        assertTrue(exception.getMessage().contains("MerchantId or MerchantKey is required"));
    }

    @Test
    @DisplayName("Method 'checkCredentials' - MerchantId or MerchantKey blank - Should Throw CredentialsException")
    void checkCredentials_BlankMerchantCredentials_ShouldThrowCredentialsException() {
        // Arrange
        ReflectionTestUtils.setField(cieloQueryService, "merchantId", "   ");
        ReflectionTestUtils.setField(cieloQueryService, "merchantKey", "");
        // Act + Assert
        CredentialsException exception =
                assertThrows(CredentialsException.class, () -> cieloQueryService.checkCredentials());
        assertTrue(exception.getMessage().contains("should not be blank"));
    }

    @Test
    @DisplayName("Method 'checkCredentials' - MerchantId > 36 - Should Throw CredentialsException")
    void checkCredentials_MerchantIdLongerThan36_ShouldThrowCredentialsException() {
        // Arrange
        String invalidMerchantId = "12345678-1234-1234-1234-123456789012345";
        ReflectionTestUtils.setField(cieloQueryService, "merchantId", invalidMerchantId);
        // Act + Assert
        CredentialsException exception =
                assertThrows(CredentialsException.class, () -> cieloQueryService.checkCredentials());
        assertTrue(exception.getMessage().contains("MerchantId should have 36 characters"));
    }

    @Test
    @DisplayName("Method 'checkCredentials' - MerchantKey < 40 - Should Throw CredentialsException")
    void checkCredentials_MerchantKeyLessThan40_ShouldThrowCredentialsException() {
        // Arrange
        ReflectionTestUtils.setField(cieloQueryService, "merchantKey", "12345");
        // Act + Assert
        CredentialsException exception =
                assertThrows(CredentialsException.class, () -> cieloQueryService.checkCredentials());
        assertTrue(exception.getMessage().contains("MerchantKey should have 40 characters"));
    }

    @Test
    @DisplayName("Method 'checkCredentials' - MerchantId not in UUID format - Should Throw CredentialsException")
    void checkCredentials_MerchantIdNotUUIDFormat_ShouldThrowCredentialsException() {
        // Arrange
        String invalidUUID = "00000000-0000-0000-0000-00000000000Z";
        ReflectionTestUtils.setField(cieloQueryService, "merchantId", invalidUUID);
        // Act + Assert
        CredentialsException exception =
                assertThrows(CredentialsException.class, () -> cieloQueryService.checkCredentials());
        assertTrue(exception.getMessage().contains("MerchantId should be in UUID format"));
    }
}
