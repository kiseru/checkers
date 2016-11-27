package com.checkers.user;

import com.checkers.board.CheckerBoard;
import com.checkers.utils.Colour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Game {
    private BufferedReader reader;
    private PrintWriter writer;
    private CheckerBoard board;

    public Game() {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);

        writer = new PrintWriter(System.out);

        board = new CheckerBoard();
    }

    public void start() {
        User firstPlayer;

        switch (showMenu()) {
            case 1:
                firstPlayer = login();
                break;
            default:
                break;
        }
    }

    public int showMenu() {
        writer.println("Menu");
        writer.println("1. Login");
        writer.print("Enter command number: ");

        try {
            return Integer.parseInt(reader.readLine());
        } catch (IOException ex) {
            return showMenu();
        }
    }

    public User login() {
        writer.println("Enter login");

        User user = new User("Player", Colour.WHITE, board);

        try {
            String login = reader.readLine();
            user.setName(login);
        } catch (Exception ex) {}
        return user;
    }
}