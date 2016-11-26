package com.checkers.exceptions;

import com.checkers.board.Cell;

public class BlackCellNotFoundException extends CheckersException {
    public BlackCellNotFoundException(Cell cell) {
        msg = "You must make your turn only on black cells. " + cell + "is not black";
    }
}
