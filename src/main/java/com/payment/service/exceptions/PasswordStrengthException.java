package com.payment.service.exceptions;


import java.util.ArrayList;

public class PasswordStrengthException extends InternalSystemException {
    public PasswordStrengthException(String message, ArrayList<String> errors) {
        super(message);
    }
}
