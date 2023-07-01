package com.checkers.board;

import com.checkers.utils.Colour;

public class Cell {
    private int col;
    private int row;
    private Colour colour;
    private Piece piece;

    private CheckerBoard board;

    public Cell(int _row, int _col, CheckerBoard _board) {
        col = _col;
        row = _row;
        board = _board;
        piece = null;
        if ((col + row) % 2 == 0) colour = Colour.BLACK;
        else colour = Colour.WHITE;
    }

    public CheckerBoard getBoard() {
        return board;
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
        try {
            piece.setCell(this);
        } catch (NullPointerException ex) {}
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

    public int diff(Cell second) {
        if (Math.abs(getCol() - second.getCol()) == Math.abs(getRow() - second.getRow())) {
            return Math.abs(getCol() - second.getCol());
        }
        return -1;
    }

    public Cell getNear(int diffRow, int diffCol) throws ArrayIndexOutOfBoundsException {
        int row = getRow() + diffRow;
        int col = getCol() + diffCol;
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return board.getCell(getRow() + diffRow, getCol() + diffCol);
    }

    public Cell between(Cell another, CheckerBoard board) {
        int col = (getCol() + another.getCol()) / 2;
        int row = (getRow() + another.getRow()) / 2;
        return board.getCell(row, col);
    }

    @Override
    public String toString() {
        char letter = (char)('a' + getCol() - 1);
        int number = getRow();
        return "" + letter + number;
    }
}
