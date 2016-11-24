package com.checkers;

public class CheckerBoard {
    private final int SIZE_OF_BOARD = 8;
    private Cell[][] cells = new Cell[SIZE_OF_BOARD][SIZE_OF_BOARD];

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
}