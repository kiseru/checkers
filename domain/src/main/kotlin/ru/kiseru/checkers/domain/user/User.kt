package ru.kiseru.checkers.domain.user

import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.exception.CellException
import ru.kiseru.checkers.domain.exception.ConvertCellException
import ru.kiseru.checkers.domain.exception.PieceException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.convertColumn
import ru.kiseru.checkers.domain.utils.convertRow
import ru.kiseru.checkers.domain.utils.getCellCaption
import java.util.UUID

class User(
    val name: String,
) {

    lateinit var id: UUID

    lateinit var color: Color

    var isCanEat = false

    lateinit var board: Board

    fun makeTurn(from: String, to: String) {
        val source = convertCell(from)
        val piece = board.getPiece(source) ?: throw CellException("Cell '${getCellCaption(source)}' is empty")

        if (piece.color != color) {
            throw PieceException("Piece in '${getCellCaption(source)}' isn't yours")
        }

        val destination = convertCell(to)
        if (board.getPiece(destination) != null) {
            throw CellException("Cell '${getCellCaption(destination)}' isn't empty")
        }

        var wasEating = false
        isCanEat = board.analyze(color)
        if (isCanEat) {
            board.eat(source, destination)
            wasEating = true
        } else {
            board.move(source, destination)
        }
        isCanEat = board.analyze(color)
        if (!wasEating) {
            isCanEat = false
        }
    }

    private fun convertCell(cell: String): Pair<Int, Int> {
        if (cell.length != 2) {
            throw ConvertCellException("Can't convert '$cell' to cell")
        }

        val column = convertColumn(cell[0])
        val row = convertRow(cell[1])
        return row to column
    }

    override fun toString(): String =
        name
}
