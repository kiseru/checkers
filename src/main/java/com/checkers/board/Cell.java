package com.checkers.board;

import com.checkers.utils.Colour;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Cell {

    private final int row;

    private final int col;

    private final CheckerBoard board;

    private Piece piece;

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null) {
            piece.setCell(this);
        }
    }

    public Colour getColor() {
        if (((col + row) & 1) == 0) {
            return Colour.BLACK;
        } else {
            return Colour.WHITE;
        }
    }

    public String getMap() {
        return toString();
    }

    public int diff(Cell second) {
        if (Math.abs(getCol() - second.getCol()) == Math.abs(getRow() - second.getRow())) {
            return Math.abs(getCol() - second.getCol());
        }
        return -1;
    }

    public Cell getNear(int diffRow, int diffCol) {
        int row = getRow() + diffRow;
        int col = getCol() + diffCol;
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return board.getCell(row, col);
    }

    public Cell between(Cell another, CheckerBoard board) {
        int col = (getCol() + another.getCol()) / 2;
        int row = (getRow() + another.getRow()) / 2;
        return board.getCell(row, col);
    }

    @Override
    public String toString() {
        char letter = (char) ('a' + col - 1);
        return String.valueOf(letter) + row;
    }
}
