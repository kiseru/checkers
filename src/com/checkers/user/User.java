package com.checkers.user;

import com.checkers.board.Cell;
import com.checkers.board.CheckerBoard;
import com.checkers.exceptions.*;
import com.checkers.utils.Colour;

import java.io.InputStreamReader;
import java.io.BufferedReader;

public class User implements IUser {
    private String name;
    private Colour colour;
    private BufferedReader reader;
    private CheckerBoard board;
    private boolean canEat;

    public User(String _name, Colour _colour, CheckerBoard _board) {
        name = _name;
        colour = _colour;
        board = _board;
        canEat = false;
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }

    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    public boolean isCanEat() {
        return canEat;
    }

    @Override
    public void makeTurn() throws Exception {
        System.out.println("Choice piece to make turn");

        String input = reader.readLine();
        input = input.toLowerCase();
        Cell from = getCell(input);
        if (from.getPiece() == null) throw new PieceNotFoundException(from);
        if (from.getPiece().getColour() != colour) throw new YourPieceNotFoundException();

        input = reader.readLine();
        input = input.toLowerCase();
        Cell to = getCell(input);
        if (to.getPiece() != null) throw new EmptyCellNotFoundException(to);
        boolean wasEating = false;
        board.analyze(this);
        if (canEat) {
            board.eat(from, to);
            wasEating = true;
        } else {
            board.move(from, to);
        }
        board.analyze(this);
        if (!wasEating) this.canEat = false;
        board.show();
    }

    private Cell getCell(String _cell) throws CheckersException {
        if (_cell.length() != 2) throw new CellDoesNotExistException(_cell);
        int col, row;

        if (_cell.charAt(0) >= 'a' && _cell.charAt(0) <= 'h') {
            col = _cell.charAt(0) - 'a' + 1;
        } else {
            throw new CellDoesNotExistException(_cell);
        }

        if (_cell.charAt(1) >= '1' && _cell.charAt(1) <= '8') {
            row = _cell.charAt(1) - '1' + 1;
        } else {
            throw new CellDoesNotExistException(_cell);
        }

        return board.getCell(row, col);
    }
}
