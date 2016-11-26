package com.checkers.exceptions;

public class PieceNotFoundException extends CheckersException {
    public PieceNotFoundException() {
        msg = "Piece is not found";
    }
}
