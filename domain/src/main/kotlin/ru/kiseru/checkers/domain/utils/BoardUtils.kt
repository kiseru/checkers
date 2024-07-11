package ru.kiseru.checkers.domain.utils

import ru.kiseru.checkers.domain.exception.CellNotFoundException

fun getCellCaption(cell: Pair<Int, Int>): String =
    getCellCaption(cell.first, cell.second)

fun getCellCaption(row: Int, column: Int): String =
    if (isCoordinatesExists(row, column)) {
        val letter = ('a' + column - 1)
        letter.toString() + row
    } else {
        throw CellNotFoundException(row, column)
    }

fun isCoordinatesExists(cell: Pair<Int, Int>): Boolean =
    isCoordinateExists(cell.first) && isCoordinateExists(cell.second)

fun isCoordinatesExists(row: Int, column: Int): Boolean =
    isCoordinateExists(row) && isCoordinateExists(column)

fun isCoordinateExists(coordinate: Int): Boolean =
    coordinate in 1..8
