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

public class Game {
    private BufferedReader reader;
    private PrintWriter writer;
    private CheckerBoard board;
    private User firstPlayer;
    private User secondPlayer;

    public Game() throws CheckersException {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);

        writer = new PrintWriter(System.out, true);

        board = new CheckerBoard(writer);
    }

    public void start() {
        showMenu();
        boolean firstPlayerTurn = true;
        User turnPlayer;
        while (board.isGaming()) {
            if (firstPlayerTurn) {
                turnPlayer = firstPlayer;
            } else {
                turnPlayer = secondPlayer;
            }
            try {
                do {
                    board.show();
                    writer.println("Your turn, " + turnPlayer.getName() + ":");
                    turnPlayer.makeTurn();
                    if (turnPlayer == firstPlayer) firstPlayerTurn = false;
                    else firstPlayerTurn = true;
                } while (turnPlayer.isCanEat());
            } catch (Exception ex) {
                writer.println(ex.getMessage());
            }
        }
    }

    public void showMenu() {
        try {
            writer.println("Main menu");
            writer.println("1. Multiplayer");
            writer.println("2. Exit");

            writer.println("Enter command number");
            String input = reader.readLine();

            switch (input) {
                case "":
                    throw new NothingToReadException();
                case "1":
                    showMultiplayer();
                    break;
                case "2":
                    System.exit(0);
                    break;
                default:
                    throw new CommandNotFoundException();
            }
        } catch (CheckersException ex) {
            writer.println(ex.getMessage());
            showMenu();
        } catch (IOException ex) {}

    }

    public void showMultiplayer() {
        try {
            writer.println("1. On one computer");
            writer.println("2. Back");

            writer.println("Enter command number");
            String input = reader.readLine();

            switch (input) {
                case "":
                    throw new NothingToReadException();
                case "1":
                    writer.println("First player:");
                    firstPlayer = login(Colour.WHITE);
                    writer.println("Second player:");
                    secondPlayer = login(Colour.BLACK);
                    break;
                case "2":
                    showMenu();
                    break;
                default:
                    throw new CommandNotFoundException();
            }
        } catch (CheckersException ex) {
            writer.println(ex.getMessage());
            showMultiplayer();
        } catch (IOException ex) {}
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

    public static void main(String[] args) throws CheckersException {
        Game game = new Game();
        game.start();
    }
}