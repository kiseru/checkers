package com.checkers.board;

import com.checkers.exceptions.MustEatException;
import com.checkers.exceptions.CannotMoveException;
import com.checkers.exceptions.CannotEatException;
import com.checkers.utils.Color;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public void analyzeAbilityOfMove() {
        boolean firstDirection = false;
        boolean secondDirection = false;
        boolean thirdDirection = false;
        boolean forthDirection = false;
        int i = 1;
        while (getCell().getRow() + i <= 8 && getCell().getColumn() + i <= 8 && !firstDirection) {
            if (!isAbleToMoveTo(getCell().getNear(i, i))) {
                firstDirection = false;
                break;
            }
            firstDirection = isAbleToMoveTo(getCell().getNear(i, i));
            i++;
        }
        i = 1;
        while (getCell().getRow() + i <= 8 && getCell().getColumn() - i >= 1 && !secondDirection) {
            if (!isAbleToMoveTo(getCell().getNear(i, -i))) {
                secondDirection = false;
                break;
            }
            secondDirection = isAbleToMoveTo(getCell().getNear(i, -i));
            i++;
        }
        i = 1;
        while (getCell().getRow() - i >= 1 && getCell().getColumn() + i <= 8 && !thirdDirection) {
            if (!isAbleToMoveTo(getCell().getNear(-i, i))) {
                thirdDirection = false;
                break;
            }
            thirdDirection = isAbleToMoveTo(getCell().getNear(-i, i));
            i++;
        }
        i = 1;
        while (getCell().getRow() - i >= 1 && getCell().getColumn() - i >= 1 && !forthDirection) {
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
    public void analyzeAbilityOfEat() {
        boolean firstDirection = false;
        boolean secondDirection = false;
        boolean thirdDirection = false;
        boolean forthDirection = false;
        int i = 2;
        while (getCell().getRow() + i <= 8 && getCell().getColumn() + i <= 8 && !firstDirection) {
            firstDirection = isAbleToEatTo(getCell().getNear(i, i));
            i++;
        }
        i = 2;
        while (getCell().getRow() + i <= 8 && getCell().getColumn() - i >= 1 && !secondDirection) {
            secondDirection = isAbleToEatTo(getCell().getNear(i, -i));
            i++;
        }
        i = 2;
        while (getCell().getRow() - i >= 1 && getCell().getColumn() + i <= 8 && !thirdDirection) {
            thirdDirection = isAbleToEatTo(getCell().getNear(-i, i));
            i++;
        }
        i = 2;
        while (getCell().getRow() - i >= 1 && getCell().getColumn() - i >= 1 && !forthDirection) {
            forthDirection = isAbleToEatTo(getCell().getNear(-i, -i));
            i++;
        }
        setCanEat(firstDirection || secondDirection || thirdDirection || forthDirection);
    }

    @Override
    public boolean isAbleToMoveTo(Cell destinationCell) {
        if (cell.diff(destinationCell) == -1) {
            return false;
        }

        if (!destinationCell.isEmpty()) {
            return false;
        }

        var row = cell.getRow();
        var column = cell.getColumn();
        var destinationRow = destinationCell.getRow();
        var destinationColumn = destinationCell.getColumn();
        var deltaColumn = destinationColumn - column;
        var deltaRow = destinationRow - row;
        var k = (float) deltaColumn / deltaRow;
        if (Math.abs(k) != 1) {
            return false;
        }

        var deltaColumnSign = (int) Math.signum(deltaColumn);
        var deltaRowSign = (int) Math.signum(deltaRow);

        var board = cell.getBoard();
        for (int i = 1; ; i++) {
            int currentRow = row + deltaRowSign * i;
            int currentColumn = column + deltaColumnSign * i;

            if (currentRow == destinationRow && currentColumn == destinationColumn) {
                return true;
            }

            var currentCell = board.getCell(currentRow, currentColumn);
            if (!currentCell.isEmpty()) {
                return false;
            }
        }
    }

    @Override
    public boolean isAbleToEatTo(Cell to) {
        Cell pieceCell = getCell();
        if (to.getPiece() != null) {
            return false;
        }

        if (getCell().diff(to) < 2) {
            return false;
        }

        byte signRow = (byte) ((to.getRow() - pieceCell.getRow()) / Math.abs(to.getRow() - pieceCell.getRow()));
        byte signCol = (byte) ((to.getColumn() - pieceCell.getColumn()) / Math.abs(to.getColumn() - pieceCell.getColumn()));
        int i = 1;
        int count = 0;
        while (pieceCell.getRow() + signRow * i <= 8 && pieceCell.getRow() + signRow * i >= 1 && pieceCell.getColumn() + signCol * i <= 8 && pieceCell.getColumn() + signCol * i >= 1) {
            try {
                if (pieceCell.getNear(signRow * i, signCol * i).getPiece().getColor() == pieceCell.getPiece().getColor()) {
                    return false;
                } else if (pieceCell.getNear(signRow * (i + 1), signCol * (i + 1)).getPiece() == null) {
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
            } catch (NullPointerException ex) {
            }
            try {
                if (pieceCell.getNear(signRow * i, signCol * i).getPiece() != null && pieceCell.getNear(signRow * (i + 1), signCol * (i + 1)).getPiece() != null) {
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                return false;
            }
            if (pieceCell.getNear(signRow * i, signCol * i).getPiece() != null) count++;

            i++;
        }
        if (count == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void move(Cell to) {
        if (isCanEat()) {
            throw new MustEatException();
        }

        if (!isCanMove()) {
            throw new CannotMoveException(cell, to);
        }

        if (!isAbleToMoveTo(to)) {
            throw new CannotMoveException(cell, to);
        }

        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
    }

    @Override
    public void eat(Cell to) {
        if (!isCanEat()) {
            throw new CannotEatException(cell, to);
        }

        if (!isAbleToEatTo(to)) {
            throw new CannotEatException(cell, to);
        }

        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
        byte signRow = (byte) ((to.getRow() - from.getRow()) / Math.abs(to.getRow() - from.getRow()));
        byte signCol = (byte) ((to.getColumn() - from.getColumn()) / Math.abs(to.getColumn() - from.getColumn()));
        int i = 1;
        while (from.getRow() + signRow * i != to.getRow() && from.getColumn() + signCol * i != to.getColumn()) {
            from.getNear(signRow * i, signCol * i).setPiece(null);
            i++;
        }
    }

    @Override
    public String toString() {
        if (getColor() == Color.BLACK) {
            return "&";
        } else {
            return "#";
        }
    }

    @Override
    public boolean isMan() {
        return false;
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
