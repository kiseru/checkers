package com.checkers.board;

import com.checkers.exceptions.CheckersException;
import com.checkers.user.User;
import com.checkers.utils.Color;

public class CheckerBoard {
    public static final int SIZE_OF_BOARD = 8;
    private Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];
    private int whitePieces;
    private int blackPieces;

    public CheckerBoard() {
        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                board[row][col] = new Cell(row + 1, col + 1, this);
            }
        }

        for (int row = 0; row < SIZE_OF_BOARD / 2 - 1; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColor() == Color.BLACK) {
                    Piece newPiece = new Man(Color.WHITE);
                    board[row][col].setPiece(newPiece);
                    newPiece.setCell(board[row][col]);
                }
            }
        }

        for (int row = SIZE_OF_BOARD - 1; row > SIZE_OF_BOARD / 2; row--) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColor() == Color.BLACK) {
                    Piece newPiece = new Man(Color.BLACK);
                    board[row][col].setPiece(newPiece);
                    newPiece.setCell(board[row][col]);
                }
            }
        }

        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                Piece piece = board[row][col].getPiece();
                if (piece != null) {
                    try {
                        piece.analyzeAbilityOfMove();
                        piece.analyzeAbilityOfEat();
                    } catch (CheckersException ex) {}
                }
            }
        }

        whitePieces = 12;
        blackPieces = 12;
    }

    public Cell getCell(int row, int col) {
        return board[row - 1][col - 1];
    }

    public void move(Cell from, Cell to) throws CheckersException {
        Piece userPiece = from.getPiece();
        from.getPiece().isAbleToMoveTo(to);
        userPiece.move(to);
    }

    public void eat(Cell from, Cell to) throws CheckersException {
        from.getPiece().eat(to);
    }

    public void analyze(User user) throws CheckersException {
        Color userColor = user.getColour();

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
        if (whitePieces == 0 || blackPieces == 0) return false;
        return true;
    }
}
