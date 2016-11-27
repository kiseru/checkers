package com.checkers.user;

import com.checkers.utils.Colour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Game {
    private BufferedReader reader;
    private PrintWriter writer;

    public Game() {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);

        writer = new PrintWriter(System.out);
    }

    public void start() {
        User firstPlayer;
        User secondPlayer;

        switch (showMenu()) {
            case 1:
                showMultiplayer();
                break;
            case 2:
                break;
            default:
                break;
        }

        switch (showMultiplayer()) {
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

    public int showMenu() {
        writer.println("Main menu");
        writer.println("1. Multiplayer");
        writer.println("2. Exit");

        try {
            return Integer.parseInt(reader.readLine());
        } catch (IOException ex) {
            return showMenu();
        }
    }

    public int showMultiplayer() {
        writer.println("1. On one computer");
        writer.println("2. Back");

        try {
            return Integer.parseInt(reader.readLine());
        } catch (IOException ex) {
            return showMultiplayer();
        }
    }

    public User login(Colour colour) {
        writer.println("Enter login");

        User user = new User("Player", colour);

        try {
            String login = reader.readLine();
            user.setName(login);
        } catch (Exception ex) {}
        return user;
    }
}