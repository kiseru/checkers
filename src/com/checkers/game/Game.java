package com.checkers.game;

import com.checkers.board.CheckerBoard;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Game {
    private BufferedReader reader;
    private PrintWriter writer;
    private CheckerBoard board;
    private User firstPlayer;
    private User secondPlayer;

    public Game() {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);

        writer = new PrintWriter(System.out, true);

        board = new CheckerBoard();
    }

    public void start() {
        showMenu();
    }

    public void showMenu() {
        writer.println("Main menu");
        writer.println("1. Multiplayer");
        writer.println("2. Exit");
        writer.println("Enter command number");

        int command = 2;

        try {
            command = Integer.parseInt(reader.readLine());
        } catch (IOException ex) {}

        switch (command) {
            case 1:
                showMultiplayer();
                break;
            case 2:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public void showMultiplayer() {
        writer.println("1. On one computer");
        writer.println("2. Back");
        writer.println("Enter command number");

        int command = 2;

        try {
            command = Integer.parseInt(reader.readLine());
        } catch (IOException ex) {}

        switch (command) {
            case 1:
                writer.println("First player:");
                firstPlayer = login(Colour.WHITE);
                writer.println("Second player:");
                secondPlayer = login(Colour.BLACK);
                break;
            case 2:
                showMenu();
                break;
            default:
                break;
        }
    }
    
    public User login(Colour colour) {
        writer.println("Enter login");

        User user = new User("Player", colour, board);

        try {
            String login = reader.readLine();
            user.setName(login);
        } catch (Exception ex) {}
        return user;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}