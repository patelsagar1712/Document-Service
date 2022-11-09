package com.moneyware.bank.documentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public CustomException(HttpStatus httpStatus, String message) {
        super(message);
        setHttpStatus(httpStatus);
        setMessage(message);
    }
}
