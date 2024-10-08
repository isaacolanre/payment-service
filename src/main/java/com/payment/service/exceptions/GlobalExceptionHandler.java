package com.payment.service.exceptions;


import com.payment.service.dto.RestResponse;
import com.payment.service.dto.response.ApiError;
import com.payment.service.enumerations.InternalExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.payment.service.enumerations.InternalExceptionCode.AN_ERROR_OCCURRED;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    public ResponseEntity<Object> handleInvalidMethodArgumentException(MethodArgumentNotValidException argumentNotValidException, HttpHeaders httpHeaders, HttpStatusCode statusCode, WebRequest request) {
        HashMap<String, String> errors = new HashMap<>();

        argumentNotValidException.getBindingResult().getAllErrors().forEach(e -> {
            var fieldError = ((FieldError) e).getField();
            var errorMessage = e.getDefaultMessage();
            errors.put(fieldError, errorMessage);
        });

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestResponse> handleUserNotFoundExeption(InternalSystemException ex) {

        log.info(ex.getMessage(), ex.isPrintStackTrace() ? ex.getStackTrace() : getStackTrace(ex));

        var response = new RestResponse(ex.getMessage(), HttpStatus.NOT_FOUND, InternalExceptionCode.USER_NOT_FOUND, "", Map.of());

        return new ResponseEntity<>(response, NOT_FOUND);
    }


    private String getStackTrace(InternalSystemException ex) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int x = 0; x < 5; x++) {
            var stackTraceElement = ex.getStackTrace()[x];
            stringBuilder.append(stackTraceElement).append("\n");
        }

        return stringBuilder.toString();
    }

    @ExceptionHandler({AuthenticationException.class})
    public final ResponseEntity<RestResponse> handleAuthenticationException(Exception ex) {

        if (Set.of(BadCredentialsException.class, CredentialsExpiredException.class)
                .contains(ex.getClass()))
            log.error(ex.getMessage());
        else
            log.error(ex.getMessage(), ex);

        RestResponse errorDetails = new RestResponse(ex.getMessage(), UNAUTHORIZED, AN_ERROR_OCCURRED, "", Map.of());
        return new ResponseEntity<>(errorDetails, UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorMessage = "Malformed JSON request. Please check your input.";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
