package com.checkers.board;

import com.checkers.utils.Colour;
import com.checkers.user.IUser;

public abstract class Piece {
    private Colour colour;

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }
}