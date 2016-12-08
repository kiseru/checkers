package com.checkers.board;

import com.checkers.utils.Colour;
import com.checkers.exceptions.*;

public class King extends Piece {

    public King(Colour colour) {
        setColour(colour);
    }

    @Override
    public void analyzeAbilityOfMove() throws CheckersException {
        boolean firstDirection = false;
        boolean secondDirection = false;
        boolean thirdDirection = false;
        boolean forthDirection = false;
        int i = 1;
        while (getCell().getRow() + i <= 8 && getCell().getCol() + i <= 8 && !firstDirection) {
            if (!isAbleToMoveTo(getCell().getNear(i, i))) {
                firstDirection = false;
                break;
            }
            firstDirection = isAbleToMoveTo(getCell().getNear(i, i));
            i++;
        }
        i = 1;
        while (getCell().getRow() + i <= 8 && getCell().getCol() - i >= 1 && !secondDirection) {
            if (!isAbleToMoveTo(getCell().getNear(i, -i))) {
                secondDirection = false;
                break;
            }
            secondDirection = isAbleToMoveTo(getCell().getNear(i, -i));
            i++;
        }
        i = 1;
        while (getCell().getRow() - i >= 1 && getCell().getCol() + i <= 8 && !thirdDirection) {
            if (!isAbleToMoveTo(getCell().getNear(-i, i))) {
                thirdDirection = false;
                break;
            }
            thirdDirection = isAbleToMoveTo(getCell().getNear(-i, i));
            i++;
        }
        i = 1;
        while (getCell().getRow() - i >= 1 && getCell().getCol() - i >= 1 && !forthDirection) {
            if (!isAbleToMoveTo(getCell().getNear(-i, -i))) {
                forthDirection = false;
                break;
            }
            forthDirection = isAbleToMoveTo(getCell().getNear(-i, -i));
            i++;
        }
        setCanMove(firstDirection || secondDirection || thirdDirection || forthDirection);
    }

    @Override
    public void analyzeAbilityOfEat() throws CheckersException {
        //TODO write analyzeAbilityOfEat() method
    }

    @Override
    public boolean isAbleToMoveTo(Cell to) throws CheckersException {
        Cell pieceCell = getCell();
        if (pieceCell.diff(to) == -1) return false;
        boolean firstDirection = false;
        boolean secondDirection = false;
        boolean thirdDirection = false;
        boolean forthDirection = false;

        int i = 1;
        while (pieceCell.getRow() + i <= 8 && pieceCell.getCol() + i <= 8 && !firstDirection) {
            if (pieceCell.getNear(i, i).getPiece() != null && pieceCell.getNear(i, i) != to) {
                firstDirection = false;
                break;
            }
            firstDirection = pieceCell.getNear(i, i) == to;
            i++;
        }
        i = 1;
        while (pieceCell.getRow() + i <= 8 && pieceCell.getCol() - i >= 1 && !secondDirection) {
            if (pieceCell.getNear(i, -i).getPiece() != null && pieceCell.getNear(i, -i) != to) {
                secondDirection = false;
                break;
            }
            secondDirection = pieceCell.getNear(i, -i) == to;
            i++;
        }
        i = 1;
        while (pieceCell.getRow() - i >= 1 && pieceCell.getCol() + i <= 8 && !thirdDirection) {
            if (pieceCell.getNear(-i, i).getPiece() != null && pieceCell.getNear(-i, i) != to) {
                thirdDirection = false;
                break;
            }
            thirdDirection = pieceCell.getNear(-i, i) == to;
            i++;
        }
        i = 1;
        while (pieceCell.getRow() - i >= 1 && pieceCell.getCol() - i >= 1 && !forthDirection) {
            if (pieceCell.getNear(-i, -i).getPiece() != null && pieceCell.getNear(-i, -i) != to) {
                forthDirection = false;
                break;
            }
            forthDirection = pieceCell.getNear(-i, -i) == to;
            i++;
        }
        return firstDirection || secondDirection || thirdDirection || forthDirection;
    }

    @Override
    public boolean isAbleToEatTo(Cell to) throws CheckersException {
        //TODO write isAbleToEatTo() method
    }

    @Override
    public void move(Cell to) throws CheckersException {
        if (isCanEat()) throw new CanEatException();
        if (!isCanMove()) throw new CanNotMoveException();
        if (!isAbleToMoveTo(to)) throw new CanNotMoveException();
        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
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
