package com.wcc.exception;

import com.wcc.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPostalCodeException extends RuntimeException {

    public InvalidPostalCodeException(String message, ErrorResponse errorResponse) {
        super(message);
    }

    public InvalidPostalCodeException(String message) {
        super(message);
    }
}
