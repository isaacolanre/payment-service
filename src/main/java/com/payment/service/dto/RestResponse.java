package com.payment.service.dto;

import com.payment.service.enumerations.InternalExceptionCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class RestResponse {

    private String message;
    private HttpStatus status;

    private String reference;

    private InternalExceptionCode exceptionCode;

    private Map<String, Object> extraData = new HashMap<>();

    public RestResponse(String message, HttpStatus status, InternalExceptionCode exceptionCode, String reference, Map<String, Object> extraData) {
        this.message = message;
        this.status = status;
        this.exceptionCode = exceptionCode;
        this.reference = reference;
        this.extraData = extraData;
    }
}
