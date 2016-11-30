package com.checkers.board;

import com.checkers.utils.Colour;

public class Man extends Piece {
    public Man(Colour colour) {
        setColour(colour);
    }

    @Override
    public boolean canEat() {
        return false;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public String toString() {
        if (getColour() == Colour.BLACK) {
            return "@";
        } else {
            return "$";
        }
    }
}
