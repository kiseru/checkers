package com.checkers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CellException extends RuntimeException {

    public CellException(String message) {
        super(message);
    }
}
