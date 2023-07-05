package com.checkers.game;

import com.checkers.board.CheckerBoard;
import com.checkers.user.User;

public class Room {
    private CheckerBoard board;
    private User firstPlayer;
    private User secondPlayer;
    private User turn;
    private int id;

    public Room(int _id, CheckerBoard _board) {
        id = _id;
        board = _board;
    }

    public int getId() {
        return id;
    }

    public CheckerBoard getBoard() {
        return board;
    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(User firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(User secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public User getTurn() {
        return turn;
    }

    public void setTurn(User turn) {
        this.turn = turn;
    }
}
