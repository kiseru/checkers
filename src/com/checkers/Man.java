package com.checkers;

public class Man extends Piece {
    public Man(Colour colour) {
        setColour(colour);
    }

    @Override
    void show() {
        if (getColour() == Colour.BLACK) {
            System.out.print('@');
        } else {
            System.out.print('$');
        }
    }
}
