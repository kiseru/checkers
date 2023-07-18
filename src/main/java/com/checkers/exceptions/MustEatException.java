package com.checkers.exceptions;

public class MustEatException extends RuntimeException {

    private static final String MSG_TEMPLATE = "You must eat enemy piece";

    public MustEatException() {
        super(MSG_TEMPLATE);
    }
}
