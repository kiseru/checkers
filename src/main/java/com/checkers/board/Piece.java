package com.checkers.board;

import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Colour;
import com.checkers.user.IUser;

public abstract class Piece {
    private Colour colour;
    private boolean canEat;
    private boolean canMove;
    private Cell cell;

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public boolean isCanEat() {
        return canEat;
    }

    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public abstract boolean isAbleToMoveTo(Cell to) throws CheckersException;

    public abstract boolean isAbleToEatTo(Cell to) throws CheckersException;

    public abstract void analyzeAbilityOfMove() throws CheckersException;

    public abstract void analyzeAbilityOfEat() throws CheckersException;

    public abstract void move(Cell to) throws CheckersException;

    public abstract void eat(Cell to) throws CheckersException;
}