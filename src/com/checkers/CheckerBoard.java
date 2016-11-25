package com.checkers;

public class CheckerBoard {
    private final int SIZE_OF_BOARD = 8;
    private Cell[][] cells = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];

    public CheckerBoard() {
        for (int i = 0; i < SIZE_OF_BOARD; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                cells[i][j] = new Cell(i + 1, j + 1);
            }
        }
    }

    public void show() {
        System.out.print("  ");
        for (char i = 'a'; i <= 'h'; i++) {
            System.out.print((char)i + " ");
        }
        System.out.println();
        for (int i = SIZE_OF_BOARD - 1; i >= 0; i--) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                cells[i][j].show();
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    public void move(Cell from, Cell to) throws Exception{
        if (to.getY() - from.getY() != 1 || Math.abs(to.getX() - from.getX()) != 1) throw new Exception("WrongMove");
        if (from.getX() < 0 || from.getX() > 7 || from.getY() < 0 || from.getY() > 7) throw new Exception("NonexistentCell");
        if (to.getX() < 0 || to.getX() > 7 || to.getY() < 0 || to.getY() > 7) throw new Exception("OutOfBoard");
        if (from.getPiece() == null) throw new Exception("NonexistentPeace");
        to.setPiece(from.getPiece());
        from.setPiece(null);
    }
}