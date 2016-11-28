package com.checkers.exceptions;

public class CommandNotFoundException extends CheckersException {
    public CommandNotFoundException() {
        msg = "Such command has not found";
    }
}
