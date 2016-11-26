package com.checkers;

public class CheckerBoard {
    private final static int SIZE_OF_BOARD = 8;
    private static Cell[][] board = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];

    public static void createBoard() {
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

    public static Cell getCell(int x, int y) {
        return board[x][y];
    }

    public static void show() {
        System.out.print("  ");
        for (char i = 'a'; i <= 'h'; i++) {
            System.out.print((char)i + " ");
        }
        System.out.println();
        for (int i = SIZE_OF_BOARD - 1; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                board[i][j].show();
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    public static void move(Cell from, Cell to) throws Exception {
        to.setPiece(from.getPiece());
        from.setPiece(null);
    }
}