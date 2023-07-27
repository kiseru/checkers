package com.checkers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PieceException extends RuntimeException {

    public PieceException(String message) {
        super(message);
    }
}
