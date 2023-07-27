package com.checkers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConvertCellException extends RuntimeException {

    public ConvertCellException(String message) {
        super(message);
    }
}
