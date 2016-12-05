package com.checkers.board;

import com.checkers.exceptions.*;
import com.checkers.utils.Colour;

public class Man extends Piece {

    public Man(Colour colour) {
        setColour(colour);
    }

    public void analyzeAbilityOfMove() throws CheckersException {
        boolean firstCell = false;
        boolean secondCell = false;
        if (getColour() == Colour.WHITE) {
            try {
                firstCell = isAbleToMoveTo(getCell().getNear(1, -1));
            } catch (ArrayIndexOutOfBoundsException ex) {}

            try {
                secondCell = isAbleToMoveTo(getCell().getNear(1, 1));
            } catch (ArrayIndexOutOfBoundsException ex) {}

            if (firstCell || secondCell) {
                setCanMove(true);
            } else {
                setCanMove(false);
            }
        } else {
            try {
                firstCell = isAbleToMoveTo(getCell().getNear(-1, -1));
            } catch (ArrayIndexOutOfBoundsException ex) {}

            try {
                secondCell = isAbleToMoveTo(getCell().getNear(-1, 1));
            } catch (ArrayIndexOutOfBoundsException ex) {}

            if (firstCell || secondCell) {
                setCanMove(true);
            } else {
                setCanMove(false);
            }
        }
    }

    public void analyzeAbilityOfEat() throws CheckersException {
        Cell pieceCell = getCell();
        boolean first = false;
        boolean second = false;
        boolean third = false;
        boolean fourth = false;

        try {
            first = isAbleToEatTo(pieceCell.getNear(2, 2));
        } catch (ArrayIndexOutOfBoundsException ex) {}

        try {
            second = isAbleToEatTo(pieceCell.getNear(-2, 2));
        } catch (ArrayIndexOutOfBoundsException ex) {}

        try {
            third = isAbleToEatTo(pieceCell.getNear(-2, -2));
        } catch (ArrayIndexOutOfBoundsException ex) {}

        try {
            fourth = isAbleToEatTo(pieceCell.getNear(2, -2));
        } catch (ArrayIndexOutOfBoundsException ex) {}

        if (first || second || third || fourth) setCanEat(true);
        else setCanEat(false);
    }

    public boolean isAbleToMoveTo(Cell to) throws CheckersException {
        Cell pieceCell = getCell();
        if (pieceCell.diff(to) != 1) return false;
        boolean firstCell = false;
        boolean secondCell = false;
        if (getColour() == Colour.WHITE) {
            try {
                firstCell = pieceCell.getNear(1, 1) == to;
            } catch (ArrayIndexOutOfBoundsException ex) {}

            try {
                secondCell = pieceCell.getNear(1, -1) == to;
            } catch (ArrayIndexOutOfBoundsException ex) {}

            if (firstCell || secondCell) return true;
        } else {
            try {
                firstCell = pieceCell.getNear(-1, -1) == to;
            } catch (ArrayIndexOutOfBoundsException ex) {}

            try {
                Cell cell = pieceCell.getNear(-1, 1);
                secondCell = cell == to;
            } catch (ArrayIndexOutOfBoundsException ex) {}

            if (firstCell || secondCell) return true;
        }
        return false;
    }

    public boolean isAbleToEatTo(Cell to) throws CheckersException {
        Cell pieceCell = getCell();
        if (pieceCell.diff(to) != 2) return false;
        else if (to.getPiece() != null) return false;
        Cell target = pieceCell.between(to, pieceCell.getBoard());
        if (target.getPiece() == null) return false;
        if (target.getPiece().getColour() == getColour()) return false;
        return true;
    }

    public void move(Cell to) throws CheckersException {
        if (isCanEat()) throw new CanEatException();
        if (!isCanMove()) throw new CanNotMoveException();
        if (!isAbleToMoveTo(to)) throw new CanNotMoveException();
        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
    }

    public void eat(Cell to) throws CheckersException {
        if (!isCanEat()) throw new CanNotEatException();
        if (!isAbleToEatTo(to)) throw new CanNotEatException();
        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
        from.between(to, from.getBoard()).setPiece(null);
    }

    @Override
    public String toString() {
        if (getColour() == Colour.BLACK) {
            return "@";
        } else {
            return "$";
        }
    }
}
