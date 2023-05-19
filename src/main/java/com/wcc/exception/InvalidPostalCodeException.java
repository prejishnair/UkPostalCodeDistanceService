package com.wcc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPostalCodeException extends RuntimeException {

    public InvalidPostalCodeException(String message) {
        super(message);
    }
}
