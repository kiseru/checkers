package com.checkers.exceptions;

import com.checkers.exceptions.CheckersException;

public class YourPieceNotFoundException extends CheckersException {
    public YourPieceNotFoundException() {
        msg = "It is not your piece";
    }
}
