package com.checkers.exceptions;

public class CheckersException extends Exception {
    protected String msg;

    @Override
    public String getMessage() {
        return msg;
    }
}
