package com.checkers.exceptions;

import com.checkers.board.Cell;

public class CellIsBusyException extends RuntimeException {

    private static final String MSG_TEMPLATE = "Cell %s already has the piece";

    public CellIsBusyException(Cell cell) {
        super(MSG_TEMPLATE.formatted(cell));
    }
}
