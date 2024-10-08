package com.payment.service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ExpressPayPaymentRequest {
    private String requestId;
    private String uniqueCode;
    private PaymentDetail details;

    public ExpressPayPaymentRequest(String requestId, String uniqueCode, PaymentDetail details) {
        this.requestId = requestId;
        this.uniqueCode = uniqueCode;
        this.details = details;
    }
}
