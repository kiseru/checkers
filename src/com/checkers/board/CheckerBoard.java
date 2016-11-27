package com.checkers.board;

import com.checkers.exceptions.BlackCellNotFoundException;
import com.checkers.exceptions.EmptyCellNotFoundException;
import com.checkers.exceptions.PieceNotFoundException;
import com.checkers.utils.Colour;

import java.io.PrintWriter;

public class CheckerBoard {
    private static PrintWriter writer;
    private static final int SIZE_OF_BOARD = 8;
    private static Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];

    public void createBoard(PrintWriter _writer) {
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
            writer.print("\n");
        }
    }

    public void move(Cell from, Cell to) throws Exception {
        if (from.getPiece() == null) throw new PieceNotFoundException(from);
        if (to.getColour() != Colour.BLACK) throw new BlackCellNotFoundException(to);
        if (to.getPiece() != null) throw new EmptyCellNotFoundException(to);
        to.setPiece(from.getPiece());
        from.setPiece(null);
    }
}