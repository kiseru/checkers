package com.checkers.utils;

public final class BoardUtils {

    public static boolean isCoordinateExists(int coordinate) {
        return coordinate >= 1 && coordinate <= 8;
    }
}
