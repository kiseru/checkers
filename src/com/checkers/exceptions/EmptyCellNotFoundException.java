package com.checkers.exceptions;

import com.checkers.board.Cell;

public class EmptyCellNotFoundException extends CheckersException {
    public EmptyCellNotFoundException(Cell cell) {
        msg = cell.getMap() + "is not empty";
    }
}
