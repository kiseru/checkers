package com.checkers.exceptions;

public class CanNotMoveException extends CheckersException {
    public CanNotMoveException() {
        msg = "Can not move this way";
    }
}
