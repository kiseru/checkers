package com.checkers.board;

import com.checkers.utils.Colour;

public class Cell {
    private int x;
    private int y;
    private Colour colour;
    private Piece piece;

    public Cell(int _x, int _y) {
        x = _x;
        y = _y;
        piece = null;
        if ((x + y) % 2 == 0) colour = Colour.BLACK;
        else colour = Colour.WHITE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
