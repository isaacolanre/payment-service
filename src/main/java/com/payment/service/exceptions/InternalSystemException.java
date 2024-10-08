package com.payment.service.exceptions;

import com.payment.service.enumerations.InternalExceptionCode;
import lombok.Getter;

@Getter
public class InternalSystemException extends RuntimeException{
    private InternalExceptionCode code;

    private boolean printStackTrace;

    public InternalSystemException(String message, InternalExceptionCode code) {
        super(message);
        this.code = code;
    }
    public InternalSystemException(String message, boolean printStackTrace) {
        super(message);
        this.printStackTrace = printStackTrace;
        this.code = InternalExceptionCode.AN_ERROR_OCCURED;
    }
    public InternalSystemException(String message){
        super(message);
    }

    public InternalSystemException (InternalExceptionCode exceptionCode, String message, boolean printStackTrace){
        super(message);
        this.code = exceptionCode;
        this.printStackTrace = printStackTrace;

    }
}
