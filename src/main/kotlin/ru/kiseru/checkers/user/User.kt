package ru.kiseru.checkers.user

import ru.kiseru.checkers.board.Board
import ru.kiseru.checkers.board.Cell
import ru.kiseru.checkers.exception.CellException
import ru.kiseru.checkers.exception.ConvertCellException
import ru.kiseru.checkers.exception.PieceException
import ru.kiseru.checkers.utils.Color
import ru.kiseru.checkers.utils.convertColumn
import ru.kiseru.checkers.utils.convertRow

class User(
    val name: String,
    val color: Color,
) {

    var isCanEat = false

    lateinit var board: Board

    fun makeTurn(from: String, to: String) {
        val sourceCell = convertCell(from)
        val piece = sourceCell.piece ?: throw CellException("Cell '$sourceCell' is empty")

        if (piece.color != color) {
            throw PieceException("Piece in '$sourceCell' isn't yours")
        }

        val destinationCell = convertCell(to)
        if (destinationCell.piece != null) {
            throw CellException("Cell '$destinationCell' isn't empty")
        }

        var wasEating = false
        board.analyze(this)
        if (isCanEat) {
            board.eat(sourceCell, destinationCell)
            wasEating = true
        } else {
            board.move(sourceCell, destinationCell)
        }
        board.analyze(this)
        if (!wasEating) {
            isCanEat = false
        }
    }

    private fun convertCell(cell: String): Cell {
        if (cell.length != 2) {
            throw ConvertCellException("Can't convert '$cell' to cell")
        }

        val column = convertColumn(cell[0])
        val row = convertRow(cell[1])
        return board.getCell(row, column)
    }

    override fun toString(): String =
        name
}
