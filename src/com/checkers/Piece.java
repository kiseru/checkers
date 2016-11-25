package com.checkers;

public abstract class Piece {
    private IUser user;
    private Colour colour;
    private boolean canEat;

    abstract void show();
}