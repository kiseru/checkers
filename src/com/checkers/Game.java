package com.checkers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    private static BufferedReader reader;

    public static void initialize() {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);
        start();
    }

    public static void start() {
        User firstPlayer;

        switch (show()) {
            case 1:
                firstPlayer = login();
                break;
            default:
                break;
        }
    }

    public static int show() {
        System.out.println("Menu");
        System.out.println("1. Login");
        System.out.print("Enter command number: ");

        try {
            return Integer.parseInt(reader.readLine());
        } catch (IOException ex) {
            return show();
        }
    }

    public static User login() {
        System.out.println("Enter login");

        User user = new User("Player", Colour.WHITE);

        try {
            String login = reader.readLine();
            user.setName(login);
        } catch (Exception ex) {}
        return user;
    }
}