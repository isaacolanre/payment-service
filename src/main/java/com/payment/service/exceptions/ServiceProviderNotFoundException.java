package com.payment.service.exceptions;

public class ServiceProviderNotFoundException extends RuntimeException {

    public ServiceProviderNotFoundException(String message){
        super(message);
    }
}
