package com.payment.service.dto.request;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public record PurchaseRequestDto(UUID userId, Long productId, BigDecimal amount, String phoneNumber) {
}

