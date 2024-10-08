package com.payment.service.exceptions;

public class ProcessServiceException extends InternalSystemException {
    public ProcessServiceException(String message, boolean printStackTrace) {
        super(message, printStackTrace);
    }
}
