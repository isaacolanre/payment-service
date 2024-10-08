package com.payment.service.enumerations;

public enum InternalExceptionCode {

    USER_NOT_FOUND("NOT FOUND"),
    AN_ERROR_OCCURED("PROCESSING ERROR"),
    AN_ERROR_OCCURRED("PROCESSING-ERROR-1000"),

    TWO_FACTOR_REQUIRED("TWO-FACTOR-REQUIRED-1001"),

    ID_INFORMATION_REQUIRED("ID-INFORMATION-REQUIRED-1003"),

    EXTRA_AUTHENTICATION_REQUIRED("EXTRA-AUTHENTICATION-REQUIRED-1002"),

    SUCCESSFUL("PROCESSED-SUCCESSFULLY-1004");

    private String code;

    InternalExceptionCode(String code){
        this.code = code;
    }
}
