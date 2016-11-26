package com.checkers;

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

    public void show() {
        try {
            piece.show();
        } catch (NullPointerException ex) {
            if (colour == Colour.BLACK) {
                System.out.print('b');
            } else {
                System.out.print('w');
            }
        }
    }
}
