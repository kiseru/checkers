package com.checkers.exceptions;

public class CellDoesNotExistException extends CheckersException {
    public CellDoesNotExistException(String cell) {
        msg = "Cell does not exist";
    }
}
