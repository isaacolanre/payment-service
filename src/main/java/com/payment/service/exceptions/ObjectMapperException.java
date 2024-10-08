package com.payment.service.exceptions;

public class ObjectMapperException extends InternalSystemException {
    public ObjectMapperException(String message, boolean printStackTrace) {
        super(message, printStackTrace);
    }
}
