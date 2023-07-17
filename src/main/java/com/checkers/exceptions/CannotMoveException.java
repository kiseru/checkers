package com.checkers.exceptions;

import com.checkers.board.Cell;

public class CannotMoveException extends RuntimeException {

    private static final String MSG_TEMPLATE = "Can't move from %s to %s";

    public CannotMoveException(Cell sourceCell, Cell destinationCell) {
        super(MSG_TEMPLATE.formatted(sourceCell, destinationCell));
    }
}
