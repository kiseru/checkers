package ru.kiseru.checkers.domain.utils

import ru.kiseru.checkers.domain.exception.ConvertCellException

fun isCoordinatesExists(row: Int, column: Int): Boolean {
    return isCoordinateExists(row) && isCoordinateExists(column)
}

fun isCoordinateExists(coordinate: Int): Boolean {
    return coordinate in 1..8
}

fun convertColumn(columnName: Char): Int {
    if (columnName < 'a' || columnName > 'h') {
        throw ConvertCellException("Column '$columnName' doesn't exists")
    }

    return columnName.code - 'a'.code + 1
}

fun convertRow(rowName: Char): Int {
    if (rowName < '1' || rowName > '8') {
        throw ConvertCellException("Row '$rowName' doesn't exists")
    }
    return rowName.code - '1'.code + 1
}
