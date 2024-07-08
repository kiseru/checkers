package ru.kiseru.checkers.domain.utils

import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.exception.ConvertCellException

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

fun convertColumn(columnName: Char): Int =
    if (columnName < 'a' || columnName > 'h') {
        throw ConvertCellException("Column '$columnName' doesn't exists")
    } else {
        columnName.code - 'a'.code + 1
    }

fun convertRow(rowName: Char): Int =
    if (rowName < '1' || rowName > '8') {
        throw ConvertCellException("Row '$rowName' doesn't exists")
    } else {
        rowName.code - '1'.code + 1
    }
