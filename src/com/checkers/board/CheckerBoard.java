package com.checkers.board;

import com.checkers.exceptions.*;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import java.io.PrintWriter;

public class CheckerBoard {
    private static PrintWriter writer;
    private static final int SIZE_OF_BOARD = 8;
    private static Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];
    private int whitePieces = 12;
    private int blackPieces = 12;

    public CheckerBoard(PrintWriter _writer) {
        writer = _writer;
        for (int i = 0; i < SIZE_OF_BOARD; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                board[i][j] = new Cell(i + 1, j + 1);
            }
        }

        for (int i = 0; i < SIZE_OF_BOARD / 2 - 1; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                if (board[i][j].getColour() == Colour.BLACK) {
                    board[i][j].setPiece(new Man(Colour.WHITE));
                }
            }
        }

        for (int i = SIZE_OF_BOARD - 1; i > SIZE_OF_BOARD / 2; i--) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                if (board[i][j].getColour() == Colour.BLACK) {
                    board[i][j].setPiece(new Man(Colour.BLACK));
                }
            }
        }
    }

    public Cell getCell(int x, int y) {
        return board[x][y];
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

    public void move(Cell from, Cell to, User user) throws CheckersException {
        if (from.diff(to) == 1) {
            if (from.getPiece() == null) throw new PieceNotFoundException(from);
            if (from.getPiece().getColour() != user.getColour()) throw new YourPieceNotFoundException();
            if (to.getColour() != Colour.BLACK) throw new BlackCellNotFoundException(to);
            if (to.getPiece() != null) throw new EmptyCellNotFoundException(to);
            if (from.getRow() - to.getRow() >= 0 && user.getColour() == Colour.WHITE) throw new CanNotMoveException();
            if (from.getRow() - to.getRow() <= 0 && user.getColour() == Colour.BLACK) throw new CanNotMoveException();
            if (user.isCanEat()) throw new CanEatException();
            to.setPiece(from.getPiece());
            from.setPiece(null);
        } else if (from.diff(to) == 2) {
            if (!user.isCanEat()) throw new CanNotEatException();
            Cell target = from.between(to, this);
            eat(target, from, to);
        } else throw new CanNotMoveException();
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
    }

    public void analyze(User user) {
        Colour colour = user.getColour();
        boolean canEat = false;
        for (int row = 0; row < SIZE_OF_BOARD && !canEat; row++) {
            for (int col = 0; col < SIZE_OF_BOARD && !canEat; col++) {
                if (board[row][col].getColour() == Colour.WHITE) continue;
                if (board[row][col].getPiece() == null) continue;
                if (row - 1 >= 0 && col - 1 >= 0 && board[row - 1][col - 1].getPiece() != null && board[row - 1][col - 1].getPiece().getColour() != board[row][col].getPiece().getColour()) canEat = true;
                if (row - 1 >= 0 && col + 1 < 8 && board[row - 1][col + 1].getPiece() != null && board[row - 1][col + 1].getPiece().getColour() != board[row][col].getPiece().getColour()) canEat = true;
                if (row + 1 < 8 && col + 1 < 8 && board[row + 1][col + 1].getPiece() != null && board[row + 1][col + 1].getPiece().getColour() != board[row][col].getPiece().getColour()) canEat = true;
                if (row + 1 < 8 && col - 1 >= 0 && board[row + 1][col - 1].getPiece() != null && board[row + 1][col - 1].getPiece().getColour() != board[row][col].getPiece().getColour()) canEat = true;
            }
        }
        user.setCanEat(canEat);
    }

    public boolean isGaming() {
        if (whitePieces == 0 || blackPieces == 0) return false;
        return true;
    }
}