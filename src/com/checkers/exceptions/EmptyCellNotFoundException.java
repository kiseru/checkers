package com.checkers.exceptions;

public class EmptyCellNotFoundException extends CheckersException {
    public EmptyCellNotFoundException() {
        msg = "Empty cell is not found";
    }
}
