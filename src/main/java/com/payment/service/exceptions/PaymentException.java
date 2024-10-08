package com.payment.service.exceptions;

public class PaymentException extends InternalSystemException {
    public PaymentException(String message, boolean printStackTrace) {
        super(message);
    }
}
