package com.checkers;

public class Cell {
    private int x;
    private int y;
    private Colour colour;
    private Piece piece;

    public Cell(int _x, int _y) {
        this(_x, _y, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() throws NullPointerException {
        if (piece == null) throw new NullPointerException();
        else return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
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
