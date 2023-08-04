package com.checkers.utils;

import com.checkers.exceptions.ConvertCellException;

public final class BoardUtils {

    public static boolean isCoordinatesExists(int row, int column) {
        return isCoordinateExists(row) && isCoordinateExists(column);
    }

    public static boolean isCoordinateExists(int coordinate) {
        return coordinate >= 1 && coordinate <= 8;
    }

    public static int convertColumn(char columnName) {
        if (columnName < 'a' || columnName > 'h') {
            throw new ConvertCellException("Column '%s' doesn't exists".formatted(columnName));
        }

        return columnName - 'a' + 1;
    }

    public static int convertRow(char rowName) {
        if (rowName < '1' || rowName > '8') {
            throw new ConvertCellException("Row '%s' doesn't exists".formatted(rowName));
        }

        return rowName - '1' + 1;
    }
}
