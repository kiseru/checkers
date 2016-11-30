package com.checkers.board;

import com.checkers.exceptions.CanNotMoveException;
import com.checkers.utils.Colour;

public class Cell {
    private int col;
    private int row;
    private Colour colour;
    private Piece piece;

    public Cell(int _row, int _col) {
        col = _col;
        row = _row;
        piece = null;
        if ((col + row) % 2 == 0) colour = Colour.BLACK;
        else colour = Colour.WHITE;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Piece getPiece() throws NullPointerException {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Colour getColour() {
        return colour;
    }

    public String getMap() {
        String result = "";
        result += (char) ('a' + getCol() - 1);
        result += getRow();
        return result;
    }

    @Override
    public String toString() {
        try {
            return piece.toString();
        } catch (NullPointerException ex) {
            if (colour == Colour.BLACK) {
                return "*";
            } else {
                return "^";
            }
        }
    }

    public int diff(Cell second) throws CanNotMoveException {
        if (Math.abs(getCol() - second.getCol()) == Math.abs(getRow() - second.getRow())) {
            return Math.abs(getCol() - second.getCol());
        }
        throw new CanNotMoveException();
    }

    public Cell between(Cell another, CheckerBoard board) {
        int col = (getCol() + another.getCol()) / 2;
        int row = (getRow() + another.getRow()) / 2;
        return board.getCell(row - 1, col - 1);
    }
}
