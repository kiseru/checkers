package com.checkers.room;

import com.checkers.board.Board;
import com.checkers.user.User;

public class Room {

    private final int id;

    private final Board board;

    private User firstPlayer;

    private User secondPlayer;

    private User turn;

    public Room(int id, Board board) {
        this.id = id;
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public Board getBoard() {
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
