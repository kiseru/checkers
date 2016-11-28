package com.checkers.exceptions;

public class NothingToReadException extends CheckersException {
    public NothingToReadException() {
        msg = "Can not read anything";
    }
}
