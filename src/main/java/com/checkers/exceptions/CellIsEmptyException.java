package com.checkers.exceptions;

import com.checkers.board.Cell;

public class CellIsEmptyException extends RuntimeException {

    private static final String MSG_TEMPLATE = "Cell %s doesn't have any piece";

    public CellIsEmptyException(Cell cell) {
        super(MSG_TEMPLATE.formatted(cell));
    }
}
