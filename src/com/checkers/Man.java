package com.checkers;

public class Man extends Piece {
    public Man(Colour colour) {
        setColour(colour);
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
