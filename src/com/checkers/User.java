package com.checkers;

import java.io.InputStreamReader;
import java.io.BufferedReader;

public class User implements IUser {
    private String name;
    private Colour colour;
    private BufferedReader reader;

    public User(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
        InputStreamReader streamReader = new InputStreamReader(System.in);
        reader = new BufferedReader(streamReader);
    }

    public String getName() {
        return name;
    }

    @Override
    public void makeTurn() {
        try {
            System.out.println("Choice piece to make turn");

            String input = reader.readLine();
            input = input.toLowerCase();
            Cell from = getCell(input);

            input = reader.readLine();
            input = input.toLowerCase();
            Cell to = getCell(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Cell getCell(String _cell) throws Exception {
        if (_cell.length() != 2) throw new Exception();
        int x, y;

        switch (_cell.charAt(0)) {
            case 'a':
                x = 0;  break;
            case 'b':
                x = 1;  break;
            case 'c':
                x = 2;  break;
            case 'd':
                x = 3;  break;
            case 'e':
                x = 4;  break;
            case 'f':
                x = 5;  break;
            case 'g':
                x = 6;  break;
            case 'h':
                x = 7;  break;
            default:
                throw new Exception();
        }

        switch (_cell.charAt(1)) {
            case '1':
                y = 0;  break;
            case '2':
                y = 1;  break;
            case '3':
                y = 2;  break;
            case '4':
                y = 3;  break;
            case '5':
                y = 4;  break;
            case '6':
                y = 5;  break;
            case '7':
                y = 6;  break;
            case '8':
                y = 7;  break;
            default:
                throw new Exception();
        }

        return new Cell(x, y);
    }
}
