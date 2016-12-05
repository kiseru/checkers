package com.checkers.board;

import com.checkers.exceptions.CheckersException;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import java.io.PrintWriter;

public class CheckerBoard {
    private PrintWriter writer;
    private static final int SIZE_OF_BOARD = 8;
    private Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];
    private int whitePieces;
    private int blackPieces;

    public CheckerBoard(PrintWriter _writer) throws CheckersException {
        writer = _writer;
        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                board[row][col] = new Cell(row + 1, col + 1, this);
            }
        }

        for (int row = 0; row < SIZE_OF_BOARD / 2 - 1; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColour() == Colour.BLACK) {
                    Piece newPiece = new Man(Colour.WHITE);
                    board[row][col].setPiece(newPiece);
                    newPiece.setCell(board[row][col]);
                }
            }
        }

        for (int row = SIZE_OF_BOARD - 1; row > SIZE_OF_BOARD / 2; row--) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColour() == Colour.BLACK) {
                    Piece newPiece = new Man(Colour.BLACK);
                    board[row][col].setPiece(newPiece);
                    newPiece.setCell(board[row][col]);
                }
            }
        }

        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                Piece piece = board[row][col].getPiece();
                if (piece != null) {
                    piece.analyzeAbilityOfMove();
                    piece.analyzeAbilityOfEat();
                }
            }
        }

        whitePieces = 12;
        blackPieces = 12;
    }

    public Cell getCell(int row, int col) throws ArrayIndexOutOfBoundsException {
        return board[row][col];
    }

    public void show() {
        writer.print("  ");
        for (char i = 'a'; i <= 'h'; i++) {
            writer.print((char)i + " ");
        }
        writer.println();
        for (int i = SIZE_OF_BOARD - 1; i >= 0; i--) {
            writer.print((i + 1) + " ");
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                writer.print(board[i][j]);
                writer.print(" ");
            }
            writer.println();
        }
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
        Colour userColour = user.getColour();

        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getPiece() != null) {
                    board[row][col].getPiece().analyzeAbilityOfMove();
                    board[row][col].getPiece().analyzeAbilityOfEat();
                }
            }
        }

        boolean isCanEat = false;
        for (int row = 0; row < SIZE_OF_BOARD && !isCanEat; row++) {
            for (int col = 0; col < SIZE_OF_BOARD && !isCanEat; col++) {
                Cell cell = getCell(row, col);
                if (cell.getColour() == Colour.WHITE) continue;
                Piece userPiece = cell.getPiece();
                if (userPiece == null) continue;
                if (userPiece.getColour() == userColour && userPiece.isCanEat()) {
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