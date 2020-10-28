package com.kiseru.checkers.models;

public class Board {
    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(" ");
        for (var i = 0; i < 8; i++) {
            stringBuilder.append(((char) ('a' + i)));
        }
        stringBuilder.append("\n");

        for (var i = 8; i > 0; i--) {
            stringBuilder.append(i);
            for (var j = 1; j <= 8; j++) {
                if (((i + j) & 1) == 1) {
                    stringBuilder.append(" ");
                } else {
                    stringBuilder.append("#");
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}