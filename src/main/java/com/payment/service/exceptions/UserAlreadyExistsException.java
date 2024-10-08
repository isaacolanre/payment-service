package com.payment.service.exceptions;

public class UserAlreadyExistsException extends InternalSystemException{
    public UserAlreadyExistsException(String message, boolean printStackTrace) {
        super(message);

    }
}
