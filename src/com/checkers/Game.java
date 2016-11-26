package com.checkers;

import java.util.Scanner;

public static class Game {
    public static void show() {
        System.out.println("Menu");
        System.out.println("1. Login");
        System.out.print("Enter command number: ");

        Scanner scan = new Scanner(System.in);
        int commandNumber = scan.nextInt();

        switch (commandNumber) {
            case 1:
                login();
                break;
            default:
                show();
                break;
        }

        scan.close();
    }

    public static void login() {
        System.out.println("Enter login");

        Scanner scan = new Scanner(System.in);
        String login = scan.nextLine();
        User user = new User(login, Colour.WHITE);

        scan.close();
    }
}