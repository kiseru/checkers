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
            if (from.getPiece() != null && to.getPiece() == null) {
                CheckerBoard.move(from, to);
            } else {
                throw new Exception("wrong move");
            }

            CheckerBoard.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Cell getCell(String _cell) throws Exception {
        if (_cell.length() != 2) throw new Exception();
        int col, row;

        switch (_cell.charAt(0)) {
            case 'a':
                col = 0;  break;
            case 'b':
                col = 1;  break;
            case 'c':
                col = 2;  break;
            case 'd':
                col = 3;  break;
            case 'e':
                col = 4;  break;
            case 'f':
                col = 5;  break;
            case 'g':
                col = 6;  break;
            case 'h':
                col = 7;  break;
            default:
                throw new Exception();
        }

        switch (_cell.charAt(1)) {
            case '1':
                row = 0;  break;
            case '2':
                row = 1;  break;
            case '3':
                row = 2;  break;
            case '4':
                row = 3;  break;
            case '5':
                row = 4;  break;
            case '6':
                row = 5;  break;
            case '7':
                row = 6;  break;
            case '8':
                row = 7;  break;
            default:
                throw new Exception();
        }

        return CheckerBoard.getCell(row, col);
    }
}
