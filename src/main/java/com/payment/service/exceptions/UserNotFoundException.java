package com.payment.service.exceptions;


import com.payment.service.enumerations.InternalExceptionCode;

public class UserNotFoundException extends InternalSystemException {

    public UserNotFoundException(String message){
        super(message, InternalExceptionCode.USER_NOT_FOUND);
    }
    public UserNotFoundException(String message, boolean printStackTrace){
        super(message, printStackTrace);
    }
}
