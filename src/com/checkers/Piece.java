package com.checkers;

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

    abstract void show();
}