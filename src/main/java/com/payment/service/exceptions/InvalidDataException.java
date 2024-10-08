package com.payment.service.exceptions;

public class InvalidDataException extends InternalSystemException {
    public InvalidDataException(String message, boolean printStackTrace) {
        super(message);
    }
}
