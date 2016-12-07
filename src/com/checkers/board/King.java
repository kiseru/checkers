package com.checkers.board;

import com.checkers.utils.Colour;
import com.checkers.exceptions.*;

public class King extends Piece {

    public King(Colour colour) {
        setColour(colour);
    }

    @Override
    public void analyzeAbilityOfMove() throws CheckersException {
        //TODO write analyzeAbilityOfMove() method
    }

    @Override
    public void analyzeAbilityOfEat() throws CheckersException {
        //TODO write analyzeAbilityOfEat() method
    }

    @Override
    public boolean isAbleToMoveTo(Cell to) throws CheckersException {
        //TODO write isAbleToMoveTo() method
    }

    @Override
    public boolean isAbleToEatTo(Cell to) throws CheckersException {
        //TODO write isAbleToEatTo() method
    }

    @Override
    public void move(Cell to) throws CheckersException {
        //TODO write move() method
    }

    @Override
    public void eat(Cell to) throws CheckersException {
        //TODO write eat() method
    }

    @Override
    public String toString() {
        if (getColour() == Colour.BLACK) {
            return "&";
        } else {
            return "#";
        }
    }
}
