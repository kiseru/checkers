package ru.kiseru.checkers.board

import ru.kiseru.checkers.utils.Color
import ru.kiseru.checkers.utils.isCoordinatesExists
import kotlin.math.abs

data class Cell(
    val row: Int,
    val column: Int,
    val board: Board,
) {

    var piece: Piece? = null
        set(value) {
            field = value
            if (value != null) {
                value.cell = this
            }
        }

    val color: Color
        get() = if ((column + row) % 2 == 0) {
            Color.BLACK
        } else {
            Color.WHITE
        }

    fun getNear(diffRow: Int, diffColumn: Int): Cell {
        val rowToFind = row + diffRow
        val columnToFind = column + diffColumn
        if (isCoordinatesExists(rowToFind, columnToFind)) {
            return board.getCell(rowToFind, columnToFind)
        }

        throw ArrayIndexOutOfBoundsException()
    }

    fun diff(second: Cell): Int {
        if (abs(column - second.column) == abs(row - second.row)) {
            return abs(column - second.column)
        }

        return -1
    }

    fun between(another: Cell, board: Board): Cell {
        val colToFind = (column + another.column) / 2
        val rowToFind = (row + another.row) / 2
        return board.getCell(rowToFind, colToFind)
    }

    override fun toString(): String {
        val letter = ('a' + column - 1)
        return letter.toString() + row
    }
}