package com.payment.service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PaymentDetail {
    private String phoneNumber;
    private BigDecimal amount;

    public PaymentDetail(String phoneNumber, BigDecimal amount) {
        this.phoneNumber = phoneNumber;
        this.amount = amount;
    }
}
