package com.checkers.game;

import com.checkers.board.CheckerBoard;
import com.checkers.exceptions.CheckersException;
import com.checkers.exceptions.CommandNotFoundException;
import com.checkers.exceptions.NothingToReadException;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

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