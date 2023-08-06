package com.checkers.user;

import com.checkers.board.Board;
import com.checkers.board.Cell;
import com.checkers.exceptions.CellException;
import com.checkers.exceptions.ConvertCellException;
import com.checkers.exceptions.PieceException;
import com.checkers.utils.BoardUtils;
import com.checkers.utils.Color;

public class User {

    private final String name;

    private final Color color;

    private final Board board;

    private boolean isCanEat = false;

    public User(String name, Color color, Board board) {
        this.name = name;
        this.color = color;
        this.board = board;
    }

    public void makeTurn(String from, String to) {
        Cell sourceCell = convertCell(from);
        if (sourceCell.isEmpty()) {
            throw new CellException("Cell '%s' is empty".formatted(sourceCell));
        }

        var piece = sourceCell.getPiece();
        if (piece.getColor() != color) {
            throw new PieceException("Piece in '%s' isn't yours".formatted(sourceCell));
        }

        Cell destinationCell = convertCell(to);
        if (!destinationCell.isEmpty()) {
            throw new CellException("Cell '%s' isn't empty".formatted(destinationCell));
        }

        boolean wasEating = false;
        board.analyze(this);
        if (isCanEat) {
            board.eat(sourceCell, destinationCell);
            wasEating = true;
        } else {
            board.move(sourceCell, destinationCell);
        }
        board.analyze(this);
        if (!wasEating) {
            isCanEat = false;
        }
    }

    private Cell convertCell(String cell) {
        if (cell.length() != 2) {
            throw new ConvertCellException("Can't convert '%s' to cell".formatted(cell));
        }

        var column = BoardUtils.convertColumn(cell.charAt(0));
        var row = BoardUtils.convertRow(cell.charAt(1));
        return board.getCell(row, column);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public boolean isCanEat() {
        return isCanEat;
    }

    public void setCanEat(boolean isCanEat) {
        this.isCanEat = isCanEat;
    }
}
