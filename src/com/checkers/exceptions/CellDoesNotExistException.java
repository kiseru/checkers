package com.checkers.exceptions;

public class CellDoesNotExistException extends CheckersException {
    public CellDoesNotExistException() {
        msg = "Cell does not exist";
    }
}
