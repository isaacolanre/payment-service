package com.payment.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends InternalSystemException {

  public TokenRefreshException(String token, String message, boolean printStackTrace) {
    super(String.format("Failed for [%s]: %s", token, message), printStackTrace);
  }
}
