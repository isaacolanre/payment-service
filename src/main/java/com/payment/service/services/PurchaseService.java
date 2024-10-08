package com.payment.service.services;

import com.payment.service.clients.ExpressPayServiceFeignClient;
import com.payment.service.dto.request.ExpressPayPaymentRequest;
import com.payment.service.dto.request.PaymentDetail;
import com.payment.service.dto.request.PurchaseRequestDto;
import com.payment.service.dto.response.PurchaseResponseDto;
import com.payment.service.enumerations.TransactionStatus;
import com.payment.service.exceptions.PaymentException;
import com.payment.service.exceptions.RecordNotFoundException;
import com.payment.service.models.*;
import com.payment.service.repository.*;
import com.payment.service.utils.PaymentRequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseService {

    private final AccountRepository accountRepository;

    private final ProductRepository productRepository;


    private final ServiceProviderRepository serviceProviderRepository;


    private final TransactionRepository transactionRepository;

    private final AppUserRepository userRepository;

    private final ExpressPayServiceFeignClient expressPayServiceFeignClient;

    private final PaymentRequestUtils paymentRequestUtils;
    @Value("${api.public.key}")
    private String publicKey;

    public PurchaseService(AccountRepository accountRepository, ProductRepository productRepository, ServiceProviderRepository serviceProviderRepository, TransactionRepository transactionRepository, AppUserRepository userRepository, ExpressPayServiceFeignClient expressPayServiceFeignClient, PaymentRequestUtils paymentRequestUtils) {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.expressPayServiceFeignClient = expressPayServiceFeignClient;
        this.paymentRequestUtils = paymentRequestUtils;
    }

    public String purchaseProduct(PurchaseRequestDto purchaseRequest) {

        UUID userId = purchaseRequest.userId();
        AppUser user = userRepository.findByPublicId(userId).
                orElseThrow(() -> new RecordNotFoundException("Account not found for user ID " + userId + " Contact Admin", false));

        Account transactingUserAccount = user.getAccounts().stream().filter(acc -> acc.isPrimaryAccount())
                .findFirst().orElseThrow(() -> new RecordNotFoundException("Account not found for user ID " + userId + " Contact Admin", false));

        Optional<Product> optionalProduct = productRepository.findById(purchaseRequest.productId());
        if (optionalProduct.isEmpty()) {
            throw new RecordNotFoundException("Product not found with ID: " + purchaseRequest.productId(), false);
        }
        Product product = optionalProduct.get();

        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository.findActiveServiceProviderByProduct(product.getId());
        if (optionalServiceProvider.isEmpty()) {
            throw new RecordNotFoundException("No active service provider found for product: " + product.getName(), false);
        }
        ServiceProvider serviceProvider = optionalServiceProvider.get();

        Optional<Account> optionalServiceProviderAccount = accountRepository.findAccountByLastname(serviceProvider.getName());
        if (optionalServiceProviderAccount.isEmpty()) {
            throw new RecordNotFoundException("No account found for service provider: " + serviceProvider.getName(), false);
        }
        Account serviceProviderAccount = optionalServiceProviderAccount.get();

        Transaction transaction = new Transaction();
        transaction.setAccount(transactingUserAccount);
        transaction.setProduct(product);
        transaction.setServiceProvider(serviceProvider);
        transaction.setAmount(product.getValue());
        transaction.setCurrency(transactingUserAccount.getCurrency().getCurrencyCode());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionType("PURCHASE");
        transactionRepository.save(transaction);


        PaymentDetail detail = new PaymentDetail(purchaseRequest.phoneNumber(), purchaseRequest.amount());
        ExpressPayPaymentRequest payPaymentRequest =
                new ExpressPayPaymentRequest(paymentRequestUtils.generateRequestId(), product.getUniqueCode(), detail);
        String authorization = "Bearer " + publicKey;
        String paymentHash = paymentRequestUtils.generateHmacHash(payPaymentRequest);

        PurchaseResponseDto externalResponse = expressPayServiceFeignClient.purchaseAirtime(authorization, paymentHash, "api",
                payPaymentRequest);

        BigDecimal transactionAmount = purchaseRequest.amount();
        if (externalResponse != null && Objects.equals(externalResponse.getResponseCode(), "00")) {
            BigDecimal newUserBalance = transactingUserAccount.getBalance().subtract(transactionAmount);
            if (newUserBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Insufficient funds in account.");
            }

            transactingUserAccount.setBalance(newUserBalance);
            accountRepository.save(transactingUserAccount);

            BigDecimal newServiceProviderBalance = serviceProviderAccount.getBalance().add(transactionAmount);
            serviceProviderAccount.setBalance(newServiceProviderBalance);
            accountRepository.save(serviceProviderAccount);

            transaction.setStatus(TransactionStatus.SUCCESS);
            transactionRepository.save(transaction);
            return "Purchase successful.";
        } else {
            transaction.setStatus(TransactionStatus.PROCESSING);
            transactionRepository.save(transaction);
            throw new PaymentException("Purchase processing with external service.", false);
        }
    }

}

