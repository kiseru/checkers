package com.checkers;

public class CheckerBoard {
    private final int SIZE_OF_BOARD = 8;
    private Cell[][] cells = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];

    public CheckerBoard() {
        for (int i = 0; i < SIZE_OF_BOARD; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public CheckerBoard(Cell[][] _cells) {
        for (int i = 0; i < SIZE_OF_BOARD; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                cells[i][j] = new Cell(_cells[i][j]);
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] _cells) {
        for (int i = 0; i < SIZE_OF_BOARD; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                cells[i][j] = new Cell(_cells[i][j]);
            }
        }
    }

    public void setCell(Cell cell) {
        cells[cell.getY()][cell.getX()] = new Cell(cell);
    }

    public void show() {
        for (char i = 'a'; i <= 'h'; i++) {
            System.out.print((char)i + " ");
        }
        System.out.println("\n");
        for (int i = 0; i < SIZE_OF_BOARD; i++) {
            for (int j = 0; j < SIZE_OF_BOARD; j++) {
                System.out.print((8 - i) + " ");
                cells[i][j].show();
            }
            System.out.print("\n");
        }
    }

    public void move(Cell from, Cell to) {
        from.getPiece().move(to);
    }
}