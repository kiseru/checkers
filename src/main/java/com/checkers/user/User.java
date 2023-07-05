package com.checkers.user;

import com.checkers.board.Cell;
import com.checkers.board.CheckerBoard;
import com.checkers.exceptions.*;
import com.checkers.utils.Color;

public class User implements IUser {
    private String name;
    private Color color;
    private CheckerBoard board;
    private boolean canEat;

    public User(String _name, Color _color, CheckerBoard _board) {
        name = _name;
        color = _color;
        board = _board;
        canEat = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Color getColour() {
        return color;
    }

    public void setCanEat(boolean canEat) {
        this.canEat = canEat;
    }

    public boolean isCanEat() {
        return canEat;
    }

    @Override
    public void makeTurn(String _from, String _to) throws CheckersException {
        Cell from = getCell(_from);
        if (from.getPiece() == null) throw new PieceNotFoundException(from);
        if (from.getPiece().getColor() != color) throw new YourPieceNotFoundException();

        Cell to = getCell(_to);
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

    @Override
    public String toString() {
        return getName();
    }
}
