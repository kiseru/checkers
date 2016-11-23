package com.checkers;

public class Cell {
    private int x;
    private int y;
    private Piece piece;

    public Cell(int _x, int _y) {
        this(_x, _y, null);
    }

    public Cell(int _x, int _y, Piece _piece) {
        x = _x;
        y = _y;
        piece = _piece;
    }

    public void show() {
        try {
            piece.show();
        } catch (NullPointerException ex) {}
    }
}
