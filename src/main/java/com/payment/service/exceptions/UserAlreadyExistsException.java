package com.payment.service.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message, boolean printStackTrace) {
        super(message);

    }
}
