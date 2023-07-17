package com.checkers.user;

import com.checkers.board.Cell;
import com.checkers.board.Board;
import com.checkers.exceptions.*;
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

    public void makeTurn(String from, String to) throws CheckersException {
        Cell cellFrom = getCell(from);
        if (cellFrom.getPiece() == null) {
            throw new PieceNotFoundException(cellFrom);
        }

        if (cellFrom.getPiece().getColor() != color) {
            throw new YourPieceNotFoundException();
        }

        Cell cellTo = getCell(to);
        if (cellTo.getPiece() != null) {
            throw new EmptyCellNotFoundException(cellTo);
        }

        boolean wasEating = false;
        board.analyze(this);
        if (canEat) {
            board.eat(cellFrom, cellTo);
            wasEating = true;
        } else {
            board.move(cellFrom, cellTo);
        }
        board.analyze(this);
        if (!wasEating) {
            this.canEat = false;
        }
    }

    private Cell getCell(String cell) throws CheckersException {
        if (cell.length() != 2) {
            throw new CellDoesNotExistException(cell);
        }

        int col, row;
        if (cell.charAt(0) >= 'a' && cell.charAt(0) <= 'h') {
            col = cell.charAt(0) - 'a' + 1;
        } else {
            throw new CellDoesNotExistException(cell);
        }

        if (cell.charAt(1) >= '1' && cell.charAt(1) <= '8') {
            row = cell.charAt(1) - '1' + 1;
        } else {
            throw new CellDoesNotExistException(cell);
        }

        return board.getCell(row, col);
    }

    @Override
    public String toString() {
        return getName();
    }
}
