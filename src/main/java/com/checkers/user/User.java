package com.checkers.user;

import com.checkers.board.Cell;
import com.checkers.board.Board;
import com.checkers.exceptions.*;
import com.checkers.utils.BoardUtils;
import com.checkers.utils.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class User {

    @Getter
    private final String name;

    @Getter
    private final Color color;

    private final Board board;

    @Setter
    @Getter
    private boolean canEat = false;

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
        if (canEat) {
            board.eat(sourceCell, destinationCell);
            wasEating = true;
        } else {
            board.move(sourceCell, destinationCell);
        }
        board.analyze(this);
        if (!wasEating) {
            canEat = false;
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
}
