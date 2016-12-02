package com.checkers.board;

import com.checkers.exceptions.*;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import java.io.PrintWriter;

public class CheckerBoard {
    private PrintWriter writer;
    private static final int SIZE_OF_BOARD = 8;
    private Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];
    private int whitePieces;
    private int blackPieces;

    public CheckerBoard(PrintWriter _writer) {
        writer = _writer;
        for (int row = 0; row < SIZE_OF_BOARD; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                board[row][col] = new Cell(row + 1, col + 1, this);
            }
        }

        for (int row = 0; row < SIZE_OF_BOARD / 2 - 1; row++) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColour() == Colour.BLACK) {
                    board[row][col].setPiece(new Man(Colour.WHITE));
                }
            }
        }

        for (int row = SIZE_OF_BOARD - 1; row > SIZE_OF_BOARD / 2; row--) {
            for (int col = 0; col < SIZE_OF_BOARD; col++) {
                if (board[row][col].getColour() == Colour.BLACK) {
                    board[row][col].setPiece(new Man(Colour.BLACK));
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

    public Cell getCell(int row, int col) {
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
        userPiece.move(to);
        /*if (from.getPiece().isCanMove() && !user.isCanEat()) {
            if (from.getPiece() == null) throw new PieceNotFoundException(from);
            if (from.getPiece().getColour() != user.getColour()) throw new YourPieceNotFoundException();
            if (!from.getPiece().isCanMoveTo(to)) throw new CanNotMoveException();
            to.setPiece(from.getPiece());
            from.setPiece(null);
            int row = from.getRow();
            int col = from.getCol();
            if (row + 1 < SIZE_OF_BOARD && col + 1 < SIZE_OF_BOARD && from.getNear(1, 1).getPiece() != null) {
                from.getNear(1, 1).getPiece().analyzeAbilityOfMove();
            }
            if (row + 1 < SIZE_OF_BOARD && col - 1 >= 0 && from.getNear(1, -1).getPiece() != null) {
                from.getNear(1, -1).getPiece().analyzeAbilityOfMove();
            }
            if (row - 1 >= 0 && col - 1 >= 0 && from.getNear(-1, -1).getPiece() != null) {
                from.getNear(-1, -1).getPiece().analyzeAbilityOfMove();
            }
            if (row - 1 >= 0 && col + 1 < SIZE_OF_BOARD && from.getNear(-1, 1).getPiece() != null) {
                from.getNear(-1, 1).getPiece().analyzeAbilityOfMove();
            }
            to.getPiece().analyzeAbilityOfEat();
            analyze(user);
        } else if (from.getPiece().isCanEat()) {
            if (!user.isCanEat()) throw new CanNotEatException();
            Cell target = from.between(to, this);
            eat(target, from, to);
        } else throw new CanNotMoveException();*/
    }

    public void eat(Cell target, Cell from, Cell to) throws CheckersException {
        if (from.getPiece() == null) throw new PieceNotFoundException(from);
        if (target.getPiece() == null) throw new PieceNotFoundException(target);
        if (to.getPiece() != null) throw new EmptyCellNotFoundException(to);
        if (target.getPiece().getColour() == Colour.BLACK) blackPieces--;
        if (target.getPiece().getColour() == Colour.WHITE) whitePieces--;
        to.setPiece(from.getPiece());
        target.setPiece(null);
        from.setPiece(null);
        System.out.println(whitePieces + " " + blackPieces);
    }

    public void analyze(User user) {
        Colour userColour = user.getColour();
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