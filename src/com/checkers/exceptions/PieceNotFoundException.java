package com.checkers.exceptions;

import com.checkers.board.Cell;

public class PieceNotFoundException extends CheckersException {
    public PieceNotFoundException(Cell cell) {
        msg = "Piece is not found in" + cell.getMap();
    }
}
