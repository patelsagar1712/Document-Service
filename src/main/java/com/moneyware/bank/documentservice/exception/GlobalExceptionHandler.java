package com.moneyware.bank.documentservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> handleGenericException(final CustomException ex) {
        final ErrorResponse errorResponse =
                new ErrorResponse(LocalDateTime.now(), ex.getHttpStatus(), ex.getMessage(), null, null);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
