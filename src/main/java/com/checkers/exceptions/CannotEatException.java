package com.checkers.exceptions;

import com.checkers.board.Cell;

public class CannotEatException extends RuntimeException {

    private static final String MSG_TEMPLATE = "Can't eat from %s to %s";

    public CannotEatException(Cell sourceCell, Cell destinationCell) {
        super(MSG_TEMPLATE.formatted(sourceCell, destinationCell));
    }
}
