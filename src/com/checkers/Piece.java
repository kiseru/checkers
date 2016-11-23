package com.checkers;

public abstract class Piece {
    private IUser user;
    private Colour colour;

    abstract void move();

    abstract void eat();

    abstract void show();
}