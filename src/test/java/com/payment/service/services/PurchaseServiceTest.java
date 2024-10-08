package com.payment.service.services;

import com.payment.service.clients.ExpressPayServiceFeignClient;
import com.payment.service.dto.request.PurchaseRequestDto;
import com.payment.service.dto.response.PurchaseResponseDto;
import com.payment.service.exceptions.PaymentException;
import com.payment.service.exceptions.RecordNotFoundException;
import com.payment.service.models.Account;
import com.payment.service.models.AppUser;
import com.payment.service.models.Product;
import com.payment.service.models.ServiceProvider;
import com.payment.service.repository.*;
import com.payment.service.utils.PaymentRequestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PurchaseServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceProviderRepository serviceProviderRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private ExpressPayServiceFeignClient expressPayServiceFeignClient;

    @Mock
    private PaymentRequestUtils paymentRequestUtils;

    private UUID userId;
    private PurchaseRequestDto purchaseRequest;
    private Account account;
    private Product product;
    private ServiceProvider serviceProvider;
    private Account serviceProviderAccount;
    private PurchaseService purchaseService;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        appUser = new AppUser();
        account = new Account();
        account.setPrimaryAccount(true);
        account.setBalance(BigDecimal.valueOf(200));
        appUser.setAccounts(new ArrayList<>());
        appUser.getAccounts().add(account);

        purchaseRequest = new PurchaseRequestDto(userId, 1L, BigDecimal.valueOf(100.00), "1234567890");

        product = new Product();
        product.setId(1L);
        product.setValue(BigDecimal.valueOf(100));
        product.setUniqueCode("MTN_19399");

        serviceProvider = new ServiceProvider();
        serviceProvider.setName("Service Provider");

        serviceProviderAccount = new Account();
        serviceProviderAccount.setBalance(BigDecimal.valueOf(50));

        purchaseService = new PurchaseService(
                accountRepository,
                productRepository,
                serviceProviderRepository,
                transactionRepository,
                userRepository,
                expressPayServiceFeignClient,
                paymentRequestUtils
        );
    }

    @Test
    void testPurchaseProduct_Success() {
        when(userRepository.findByPublicId(userId)).thenReturn(Optional.of(appUser));
        when(accountRepository.findAccountByLastname(serviceProvider.getName())).thenReturn(Optional.of(serviceProviderAccount));
        when(productRepository.findById(purchaseRequest.productId())).thenReturn(Optional.of(product));
        when(serviceProviderRepository.findActiveServiceProviderByProduct(product.getId())).thenReturn(Optional.of(serviceProvider));

        PurchaseResponseDto mockResponse = new PurchaseResponseDto();
        mockResponse.setRequestId("random-request-id");
        mockResponse.setResponseCode("200");
        mockResponse.setResponseMessage("Purchase successful.");

        when(expressPayServiceFeignClient.purchaseAirtime(any(), any(), any(), any())).thenReturn(mockResponse);

        String result = purchaseService.purchaseProduct(purchaseRequest);

        assertEquals("Purchase successful.", result);
        assertEquals(BigDecimal.valueOf(100), account.getBalance());
        assertEquals(BigDecimal.valueOf(150), serviceProviderAccount.getBalance());
    }

    @Test
    void testPurchaseProduct_AccountNotFound() {
        when(userRepository.findByPublicId(userId)).thenReturn(Optional.empty());

        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> purchaseService.purchaseProduct(purchaseRequest));
        assertEquals("Account not found for user ID " + userId + " Contact Admin", thrown.getMessage());
    }

    @Test
    void testPurchaseProduct_ProductNotFound() {
        when(userRepository.findByPublicId(userId)).thenReturn(Optional.of(appUser));
        when(productRepository.findById(purchaseRequest.productId())).thenReturn(Optional.empty());

        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> purchaseService.purchaseProduct(purchaseRequest));
        assertEquals("Product not found with ID: " + purchaseRequest.productId(), thrown.getMessage());
    }


    @Test
    void testPurchaseProduct_FailureDueToAPI() {
        // Set up valid user, product, and service provider
        when(userRepository.findByPublicId(userId)).thenReturn(Optional.of(appUser));
        when(accountRepository.findAccountByLastname(serviceProvider.getName())).thenReturn(Optional.of(serviceProviderAccount));
        when(productRepository.findById(purchaseRequest.productId())).thenReturn(Optional.of(product));
        when(serviceProviderRepository.findActiveServiceProviderByProduct(product.getId())).thenReturn(Optional.of(serviceProvider));

        // Simulate a failed response from the external API
        PurchaseResponseDto mockResponse = new PurchaseResponseDto();
        mockResponse.setRequestId("random-request-id");
        mockResponse.setResponseCode("500");  // Simulate failure response code
        mockResponse.setResponseMessage("Purchase failed.");

        when(expressPayServiceFeignClient.purchaseAirtime(any(), any(), any(), any())).thenReturn(mockResponse);

        // Test that PurchaseFailedException is thrown due to the failure response
        assertThrows(PaymentException.class, () -> purchaseService.purchaseProduct(purchaseRequest));
    }

    @Test
    void testPurchaseProduct_FailureDueToNoServiceProvider() {
        when(userRepository.findByPublicId(userId)).thenReturn(Optional.of(appUser));
        when(productRepository.findById(purchaseRequest.productId())).thenReturn(Optional.of(product));

        when(serviceProviderRepository.findActiveServiceProviderByProduct(product.getId())).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> purchaseService.purchaseProduct(purchaseRequest));
    }

    @Test
    void testPurchaseProduct_FailureDueToInsufficientBalance() {
        account.setBalance(BigDecimal.valueOf(50));
        when(userRepository.findByPublicId(userId)).thenReturn(Optional.of(appUser));
        when(accountRepository.findAccountByLastname(serviceProvider.getName())).thenReturn(Optional.of(serviceProviderAccount));
        when(productRepository.findById(purchaseRequest.productId())).thenReturn(Optional.of(product));
        when(serviceProviderRepository.findActiveServiceProviderByProduct(product.getId())).thenReturn(Optional.of(serviceProvider));

        assertThrows(PaymentException.class, () -> purchaseService.purchaseProduct(purchaseRequest));
    }
}
