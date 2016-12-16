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
        boolean firstDirection = false;
        boolean secondDirection = false;
        boolean thirdDirection = false;
        boolean forthDirection = false;
        int i = 2;
        while (getCell().getRow() + i <= 8 && getCell().getCol() + i <= 8 && !firstDirection) {
            firstDirection = isAbleToEatTo(getCell().getNear(i, i));
            i++;
        }
        i = 2;
        while (getCell().getRow() + i <= 8 && getCell().getCol() - i >= 1 && !secondDirection) {
            secondDirection = isAbleToEatTo(getCell().getNear(i, -i));
            i++;
        }
        i = 2;
        while (getCell().getRow() - i >= 1 && getCell().getCol() + i <= 8 && !thirdDirection) {
            thirdDirection = isAbleToEatTo(getCell().getNear(-i, i));
            i++;
        }
        i = 2;
        while (getCell().getRow() - i >= 1 && getCell().getCol() - i >= 1 && !forthDirection) {
            forthDirection = isAbleToEatTo(getCell().getNear(-i, -i));
            i++;
        }
        setCanEat(firstDirection || secondDirection || thirdDirection || forthDirection);
    }

    @Override
    public boolean isAbleToMoveTo(Cell to) throws CheckersException {
        Cell pieceCell = getCell();
        if (pieceCell.diff(to) == -1) return false;
        if (to.getPiece() != null) return false;
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
        Cell pieceCell = getCell();
        if (to.getPiece() != null) return false;
        if (getCell().diff(to) < 2) return false;
        byte signRow = (byte)((to.getRow() - pieceCell.getRow()) / Math.abs(to.getRow() - pieceCell.getRow()));
        byte signCol = (byte)((to.getCol() - pieceCell.getCol()) / Math.abs(to.getCol() - pieceCell.getCol()));
        int i = 1;
        int count = 0;
        while (pieceCell.getRow() + signRow * i <= 8 && pieceCell.getRow() + signRow * i >= 1 && pieceCell.getCol() + signCol * i <= 8 && pieceCell.getCol() + signCol * i >= 1 ) {
            try {
                if (pieceCell.getNear(signRow * i, signCol * i).getPiece().getColour() == pieceCell.getPiece().getColour())
                    return false;
                else if (pieceCell.getNear(signRow * (i + 1), signCol * (i + 1)).getPiece() == null)
                    return true;
            } catch (ArrayIndexOutOfBoundsException ex) {} catch (NullPointerException ex) {}
            try {
                if (pieceCell.getNear(signRow * i, signCol * i).getPiece() != null && pieceCell.getNear(signRow * (i + 1), signCol * (i + 1)).getPiece() != null)
                    return false;
            } catch (ArrayIndexOutOfBoundsException ex) {return false;}
            if (pieceCell.getNear(signRow * i, signCol * i).getPiece() != null) count++;

            i++;
        }
        if (count == 0) return false;
        return true;
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
        if (!isCanEat()) throw new CanNotEatException();
        if (!isAbleToEatTo(to)) throw new CanNotEatException();
        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
        byte signRow = (byte)((to.getRow() - from.getRow()) / Math.abs(to.getRow() - from.getRow()));
        byte signCol = (byte)((to.getCol() - from.getCol()) / Math.abs(to.getCol() - from.getCol()));
        int i = 1;
        while (from.getRow() + signRow * i != to.getRow() && from.getCol() + signCol * i != to.getCol()) {
            from.getNear(signRow * i, signCol * i).setPiece(null);
            i++;
        }
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
