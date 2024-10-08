package com.payment.service.exceptions;

public class RecordNotFoundException extends InternalSystemException {
    public RecordNotFoundException(String message, boolean printStackTrace) {
        super(message);

    }
}
