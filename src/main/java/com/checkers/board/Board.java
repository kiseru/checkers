package com.checkers.board;

import com.checkers.exceptions.CannotEatException;
import com.checkers.exceptions.CannotMoveException;
import com.checkers.exceptions.CellIsBusyException;
import com.checkers.exceptions.CellIsEmptyException;
import com.checkers.user.User;
import com.checkers.utils.Color;
import lombok.Getter;

public class Board {

    private static final int SIZE_OF_BOARD = 8;

    @Getter
    private final Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];

    private int whitePieces = 12;

    private int blackPieces = 12;

    public Board() {
        createCellsInBoard();
        initWhitePieces();
        initBlackPieces();
        analyzeAbilities();
    }

    private void createCellsInBoard() {
        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                board[row][col] = new Cell(row + 1, col + 1, this);
            }
        }
    }

    private void initWhitePieces() {
        for (int row = 0; row < SIZE_OF_BOARD / 2 - 1; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColor() == Color.BLACK) {
                    Piece newPiece = new Man(Color.WHITE);
                    board[row][col].setPiece(newPiece);
                    newPiece.setCell(board[row][col]);
                }
            }
        }
    }

    private void initBlackPieces() {
        for (int row = SIZE_OF_BOARD - 1; row > SIZE_OF_BOARD / 2; row--) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColor() == Color.BLACK) {
                    Piece newPiece = new Man(Color.BLACK);
                    board[row][col].setPiece(newPiece);
                    newPiece.setCell(board[row][col]);
                }
            }
        }
    }

    private void analyzeAbilities() {
        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                Piece piece = board[row][col].getPiece();
                if (piece != null) {
                    piece.analyzeAbilityOfMove();
                    piece.analyzeAbilityOfEat();
                }
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return null;
        }

        return board[row - 1][col - 1];
    }

    public void move(Cell sourceCell, Cell destinationCell) {
        if (sourceCell.isEmpty()) {
            throw new CellIsEmptyException(sourceCell);
        }

        if (!destinationCell.isEmpty()) {
            throw new CellIsBusyException(destinationCell);
        }

        Piece piece = sourceCell.getPiece();
        if (piece.isAbleToMoveTo(destinationCell)) {
            piece.move(destinationCell);
        } else {
            throw new CannotMoveException(sourceCell, destinationCell);
        }
    }

    public void eat(Cell sourceCell, Cell destinationCell) {
        if (sourceCell.isEmpty()) {
            throw new CellIsEmptyException(sourceCell);
        }

        if (!destinationCell.isEmpty()) {
            throw new CellIsBusyException(destinationCell);
        }

        var piece = sourceCell.getPiece();
        if (piece.isAbleToEatTo(destinationCell)) {
            piece.eat(destinationCell);
        } else {
            throw new CannotEatException(sourceCell, destinationCell);
        }
    }

    public void analyze(User user) {
        Color userColor = user.getColor();

        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getPiece() != null) {
                    board[row][col].getPiece().analyzeAbilityOfMove();
                    board[row][col].getPiece().analyzeAbilityOfEat();
                    if (!board[row][col].getPiece().isCanMove()) {
                        System.out.println(row + " " + col);
                    }
                }
            }
        }

        boolean isCanEat = false;
        for (int row = 1; row <= SIZE_OF_BOARD && !isCanEat; row++) {
            for (int col = 1; col <= SIZE_OF_BOARD && !isCanEat; col++) {
                Cell cell = getCell(row, col);
                if (cell.getColor() == Color.WHITE) continue;
                Piece userPiece = cell.getPiece();
                if (userPiece == null) continue;
                if (userPiece.getColor() == userColor && userPiece.isCanEat()) {
                    isCanEat = true;
                }
            }
        }
        user.setCanEat(isCanEat);
    }

    public boolean isGaming() {
        return whitePieces != 0 && blackPieces != 0;
    }
}
