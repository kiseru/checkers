package com.checkers;

public abstract class Piece {
    private IUser user;

    abstract void move(Cell to);
    abstract void eat();
    abstract void show();
}