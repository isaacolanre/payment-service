package com.payment.service.exceptions;

public class AuthenticationException extends InternalSystemException {
    public AuthenticationException(String message, boolean printStackTrace) {
        super(message, printStackTrace);
    }
}
