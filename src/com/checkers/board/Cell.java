package com.checkers.board;

import com.checkers.utils.Colour;

public class Cell {
    private int col;
    private int row;
    private Colour colour;
    private Piece piece;

    public Cell(int _x, int _y) {
        col = _x;
        row = _y;
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
}
