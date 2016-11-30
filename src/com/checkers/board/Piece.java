package com.checkers.board;

import com.checkers.utils.Colour;
import com.checkers.user.IUser;

public abstract class Piece {
    private IUser user;
    private Colour colour;
    private boolean canEat;

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public abstract boolean canEat();
    public abstract boolean canMove();
}