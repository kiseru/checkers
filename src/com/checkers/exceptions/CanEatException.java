package com.checkers.exceptions;

public class CanEatException extends CheckersException {
    public CanEatException() {
        msg = "You must eat enemy piece";
    }
}
