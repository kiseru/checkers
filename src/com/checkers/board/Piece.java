package com.checkers.board;

import com.checkers.utils.Colour;
import com.checkers.user.IUser;

public abstract class Piece {
    private Colour colour;
    private boolean canEat;
    private boolean canMove;

    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanEat() {
        return canEat;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }
}