package com.checkers.board;

import com.checkers.exceptions.*;
import com.checkers.utils.Colour;

public class Man extends Piece {

    public Man(Colour colour) {
        setColour(colour);
    }

    public void analyzeAbilityOfMove() {
        Colour pieceColour = getColour();
        Cell cell = getCell();
        int row = cell.getRow();
        int col = cell.getCol();
        if (pieceColour == Colour.WHITE) {
            if (row + 1 < 8 && col - 1 >= 0 && cell.getNear(1, -1).getPiece() == null ||
                    row + 1 < 8 && col + 1 < 8 && cell.getNear(1, 1).getPiece() == null) {
                setCanMove(true);
            } else {
                setCanMove(false);
            }
        } else {
            if (row - 1 >= 0 && col - 1 >= 0 && cell.getNear(-1, -1).getPiece() == null ||
                    row - 1 >= 0 && col + 1 < 8 && cell.getNear(-1, 1).getPiece() == null) {
                setCanMove(true);
            } else {
                setCanMove(false);
            }
        }
    }

    public void analyzeAbilityOfEat() {
        Colour pieceColour = getColour();
        Cell cell = getCell();
        int row = cell.getRow();
        int col = cell.getCol();
        if (pieceColour == Colour.WHITE) {
            if (row + 2 < 8 && col - 2 >= 0 && cell.getNear(1, -1).getPiece() != null &&
                    cell.getNear(1, -1).getPiece().getColour() != pieceColour &&
                    cell.getNear(2, -2).getPiece() == null ||
                    row + 2 < 8 && col + 2 < 8 && cell.getNear(1, 1).getPiece() != null &&
                            cell.getNear(1, 1).getPiece().getColour() != pieceColour &&
                            cell.getNear(2, 2).getPiece() == null ||
                    row - 2 >= 0 && col - 2 >= 0 && cell.getNear(-1, -1).getPiece() != null &&
                            cell.getNear(-1, -1).getPiece().getColour() != pieceColour &&
                            cell.getNear(-2, -2).getPiece() == null ||
                    row - 2 >= 0 && col + 2 < 8 && cell.getNear(-1, 1).getPiece() != null &&
                            cell.getNear(-1, 1).getPiece().getColour() != pieceColour &&
                            cell.getNear(-2, 2).getPiece() == null) {
                setCanEat(true);
            } else {
                setCanEat(false);
            }
        }
    }

    public boolean isCanMoveTo(Cell to) throws CheckersException {
        if (isCanEat()) throw new CanEatException();
        if (to.getColour() != Colour.BLACK) throw new BlackCellNotFoundException(to);
        if (!(getColour() == Colour.WHITE && (to == getCell().getNear(1, -1) || to == getCell().getNear(1, 1))))
            return false;
        if (!(getColour() == Colour.BLACK && (to == getCell().getNear(-1, -1) || to == getCell().getNear(-1, 1))))
            return false;
        if (to.getPiece() != null) throw new EmptyCellNotFoundException(to);
        return true;
    }

    public void move(Cell to) throws CheckersException {
        Cell cell = getCell();
        boolean canMove = isCanMoveTo(to);
        if (canMove) {
            to.setPiece(this);
            cell.setPiece(null);
        } else throw new CanNotMoveException();
    }

    public void eat(Cell to) throws CheckersException {
        if (!isCanEat()) throw new CanNotMoveException();
        Cell cell = getCell();
        
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
