package com.checkers.board;

import com.checkers.exceptions.CanEatException;
import com.checkers.exceptions.CanNotEatException;
import com.checkers.exceptions.CanNotMoveException;
import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Color;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Man extends Piece {

    public Man(Color color) {
        super(color);
    }

    public void analyzeAbilityOfMove() {
        var row = cell.getRow();
        var nextRow = color == Color.WHITE ? row + 1 : row - 1;
        if (nextRow < 1 || nextRow > 8) {
            return;
        }

        var board = cell.getBoard();
        var column = cell.getCol();
        canMove = IntStream.of(column - 1, column + 1)
                .filter(this::hasCoordinate)
                .mapToObj(destinationColumn -> board.getCell(nextRow, destinationColumn))
                .anyMatch(this::isAbleToMoveTo);
    }

    public void analyzeAbilityOfEat() {
        var column = cell.getCol();
        var row = cell.getRow();
        var board = cell.getBoard();
        canEat = IntStream.of(row - 2, row + 2)
                .filter(this::hasCoordinate)
                .boxed()
                .flatMap(nextRow -> IntStream.of(column - 2, column + 2)
                        .filter(this::hasCoordinate)
                        .mapToObj(nextColumn -> board.getCell(nextRow, nextColumn)))
                .anyMatch(this::isAbleToEatTo);
    }

    private boolean hasCoordinate(int coordinate) {
        return coordinate >= 1 && coordinate <= 8;
    }

    public boolean isAbleToMoveTo(Cell destinationCell) {
        if (!destinationCell.isEmpty() || isEnemyNear() || cell.diff(destinationCell) != 1) {
            return false;
        }

        var board = cell.getBoard();
        var row = cell.getRow();
        var col = cell.getCol();
        var nextRow = color == Color.WHITE ? row + 1 : row - 1;
        return Stream.of(board.getCell(nextRow, col + 1), board.getCell(nextRow, col - 1))
                .anyMatch(nearCell -> nearCell == destinationCell);
    }

    private boolean isEnemyNear() {
        var col = cell.getCol();
        var row = cell.getRow();

        var board = cell.getBoard();
        return Stream.of(
                        board.getCell(row - 1, col - 1),
                        board.getCell(row - 1, col + 1),
                        board.getCell(row + 1, col + 1),
                        board.getCell(row + 1, col - 1)
                )
                .filter(Objects::nonNull)
                .anyMatch(this::hasEnemyIn);
    }

    private boolean hasEnemyIn(Cell cell) {
        var piece = cell.getPiece();
        if (piece == null) {
            return false;
        }

        var pieceColor = piece.getColor();
        return pieceColor != color;
    }

    public boolean isAbleToEatTo(Cell destinationCell) {
        if (cell.diff(destinationCell) != 2 || !destinationCell.isEmpty()) {
            return false;
        }

        Cell cellWithSacrifice = cell.between(destinationCell, cell.getBoard());
        var sacrificePiece = cellWithSacrifice.getPiece();
        return sacrificePiece != null && sacrificePiece.color != color;
    }

    public void move(Cell to) throws CheckersException {
        if (isCanEat()) {
            throw new CanEatException();
        }

        if (!isCanMove()) {
            throw new CanNotMoveException();
        }

        if (!isAbleToMoveTo(to)) {
            throw new CanNotMoveException();
        }

        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
        if (getColor() == Color.WHITE && to.getRow() == 8) {
            to.setPiece(new King(getColor()));
        }
        if (getColor() == Color.BLACK && to.getRow() == 1) {
            to.setPiece(new King(getColor()));
        }
    }

    public void eat(Cell to) throws CheckersException {
        if (!isCanEat()) {
            throw new CanNotEatException();
        }

        if (!isAbleToEatTo(to)) {
            throw new CanNotEatException();
        }

        Cell from = getCell();
        to.setPiece(this);
        from.setPiece(null);
        from.between(to, from.getBoard()).setPiece(null);
        if (getColor() == Color.WHITE && to.getRow() == 8) {
            to.setPiece(new King(getColor()));
        }
        if (getColor() == Color.BLACK && to.getRow() == 1) {
            to.setPiece(new King(getColor()));
        }
    }
}
