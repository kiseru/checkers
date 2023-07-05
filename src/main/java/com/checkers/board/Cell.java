package com.checkers.board;

import com.checkers.utils.Color;
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

    public Color getColor() {
        if (((col + row) & 1) == 0) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    public String getMap() {
        return toString();
    }

    public int diff(Cell second) {
        if (Math.abs(col - second.col) == Math.abs(row - second.row)) {
            return Math.abs(col - second.col);
        }
        return -1;
    }

    public Cell getNear(int diffRow, int diffCol) {
        int rowToFind = row + diffRow;
        int colToFind = col + diffCol;
        if (rowToFind < 1 || rowToFind > 8 || colToFind < 1 || colToFind > 8) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return board.getCell(rowToFind, colToFind);
    }

    public Cell between(Cell another, CheckerBoard board) {
        int colToFind = (col + another.col) / 2;
        int rowToFind = (row + another.row) / 2;
        return board.getCell(rowToFind, colToFind);
    }

    @Override
    public String toString() {
        char letter = (char) ('a' + col - 1);
        return String.valueOf(letter) + row;
    }
}
