package com.checkers.board;

import com.checkers.exceptions.CannotEatException;
import com.checkers.exceptions.CannotMoveException;
import com.checkers.exceptions.MustEatException;
import com.checkers.utils.BoardUtils;
import com.checkers.utils.Color;

import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class King extends Piece {

    public King(Color color) {
        super(color);
    }

    @Override
    public void analyzeAbilityOfMove() {
        var row = cell.getRow();
        var column = cell.getColumn();
        canMove = IntStream.iterate(1, i -> i + 1)
                .limit(7)
                .boxed()
                .flatMap(i -> Stream.of(Map.entry(i, i), Map.entry(i, -i), Map.entry(-i, -i), Map.entry(-i, i)))
                .filter(entry -> BoardUtils.isCoordinatesExists(row + entry.getKey(), column + entry.getValue()))
                .anyMatch(entry -> isAbleToMoveTo(cell.getNear(entry.getKey(), entry.getValue())));
    }

    @Override
    public void analyzeAbilityOfEat() {
        var row = cell.getRow();
        var column = cell.getColumn();
        canEat = IntStream.iterate(2, i -> i + 1)
                .limit(6)
                .boxed()
                .flatMap(i -> Stream.of(Map.entry(i, i), Map.entry(i, -i), Map.entry(-i, -i), Map.entry(-i, i)))
                .filter(entry -> BoardUtils.isCoordinatesExists(row + entry.getKey(), column + entry.getValue()))
                .anyMatch(entry -> isAbleToEatTo(cell.getNear(entry.getKey(), entry.getValue())));
    }

    @Override
    public boolean isAbleToMoveTo(Cell destinationCell) {
        if (cell.diff(destinationCell) == -1) {
            return false;
        }

        if (!destinationCell.isEmpty()) {
            return false;
        }

        if (isOnOtherDiagonal(destinationCell)) {
            return false;
        }

        var row = cell.getRow();
        var column = cell.getColumn();
        var destinationRow = destinationCell.getRow();
        var destinationColumn = destinationCell.getColumn();
        var deltaColumn = destinationColumn - column;
        var deltaRow = destinationRow - row;

        var deltaColumnSign = (int) Math.signum(deltaColumn);
        var deltaRowSign = (int) Math.signum(deltaRow);

        var board = cell.getBoard();
        for (var i = 1; ; i++) {
            var currentRow = row + deltaRowSign * i;
            var currentColumn = column + deltaColumnSign * i;

            if (currentRow == destinationRow && currentColumn == destinationColumn) {
                return true;
            }

            var currentCell = board.getCell(currentRow, currentColumn);
            if (!currentCell.isEmpty()) {
                return false;
            }
        }
    }

    @Override
    public boolean isAbleToEatTo(Cell destinationCell) {
        if (!destinationCell.isEmpty()) {
            return false;
        }

		if (isOnOtherDiagonal(destinationCell)) {
			return false;
		}

        if (cell.diff(destinationCell) < 2) {
            return false;
        }

        var row = cell.getRow();
        var column = cell.getColumn();
        var destinationRow = destinationCell.getRow();
        var destinationColumn = destinationCell.getColumn();
        var deltaColumn = destinationColumn - column;
        var deltaRow = destinationRow - row;

        var deltaColumnSign = (int) Math.signum(deltaColumn);
        var deltaRowSign = (int) Math.signum(deltaRow);

        var enemyPieceCount = 0;
        var board = cell.getBoard();
        for (var i = 1; ; i++) {
            var currentRow = row + deltaRowSign * i;
            var currentColumn = column + deltaColumnSign * i;

            if (currentColumn == destinationColumn && currentRow == destinationRow) {
                return enemyPieceCount == 1;
            }

            var currentCell = board.getCell(currentRow, currentColumn);
            if (currentCell.isEmpty()) {
                continue;
            }

            var piece = currentCell.getPiece();
            if (piece.getColor() == color) {
                return false;
            }

            enemyPieceCount++;
        }
    }

    private boolean isOnOtherDiagonal(Cell destinationCell) {
        var deltaColumn = destinationCell.getColumn() - cell.getColumn();
        var deltaRow = destinationCell.getRow() - cell.getRow();
        var k = (float) deltaColumn / deltaRow;
        return Math.abs(k) != 1;
    }

    @Override
    public void move(Cell destinationCell) {
        if (canEat) {
            throw new MustEatException();
        }

        if (!canMove) {
            throw new CannotMoveException(cell, destinationCell);
        }

        if (!isAbleToMoveTo(destinationCell)) {
            throw new CannotMoveException(cell, destinationCell);
        }

        destinationCell.setPiece(this);
        cell.setPiece(null);
    }

    @Override
    public void eat(Cell destinationCell) {
        if (!canEat) {
            throw new CannotEatException(cell, destinationCell);
        }

        if (!isAbleToEatTo(destinationCell)) {
            throw new CannotEatException(cell, destinationCell);
        }

        var sacrificedPiece = getSacrificedPiece(destinationCell)
                .orElseThrow(() -> new CannotEatException(cell, destinationCell));
        var sacrificedPieceCell = sacrificedPiece.getCell();
        sacrificedPieceCell.setPiece(null);
        destinationCell.setPiece(this);
        cell.setPiece(null);
    }

    private Optional<Piece> getSacrificedPiece(Cell destinationCell) {
        var destinationRow = destinationCell.getRow();
        var destinationColumn = destinationCell.getColumn();
        var sourceRow = cell.getRow();
        var sourceColumn = cell.getColumn();

        var signRow = (int) Math.signum(destinationRow - sourceRow);
        var signCol = (int) Math.signum(destinationColumn - sourceColumn);

        var board = cell.getBoard();
        for (var i = 1; ; i++) {
            var currentRow = sourceRow + signRow * i;
            var currentColumn = sourceColumn + signCol * i;

            if (currentRow == destinationRow && currentColumn == destinationColumn) {
                return Optional.empty();
            }

            var currentCell = board.getCell(currentRow, currentColumn);
            if (currentCell.isEmpty()) {
                continue;
            }

            var piece = currentCell.getPiece();
            if (piece.getColor() == color) {
                return Optional.empty();
            } else {
                return Optional.of(piece);
            }
        }
    }

    @Override
    public String toString() {
        if (color == Color.BLACK) {
            return "&";
        } else {
            return "#";
        }
    }

    @Override
    public boolean isMan() {
        return false;
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
