package com.checkers.exceptions;

public class CanNotEatException extends CheckersException {
    public CanNotEatException() {
        msg = "You can not eat";
    }
}
