package ru.kiseru.checkers.domain.user

import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.exception.CellException
import ru.kiseru.checkers.domain.exception.ConvertCellException
import ru.kiseru.checkers.domain.exception.PieceException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.getCellCaption
import java.util.UUID

class User(
    val name: String,
) {

    lateinit var id: UUID

    lateinit var color: Color

    lateinit var board: Board

    fun makeTurn(from: String, to: String): Boolean {
        val source = convertCell(from)
        checkPieceOwner(source)
        val destination = convertCell(to)
        return makeTurn(source, destination)
    }

    private fun checkPieceOwner(pieceLocation: Pair<Int, Int>) {
        val piece = board.getPiece(pieceLocation)
            ?: throw CellException("Cell '${getCellCaption(pieceLocation)}' is empty")
        if (piece.color != color) {
            throw PieceException("Piece in '${getCellCaption(pieceLocation)}' isn't yours")
        }
    }

    private fun makeTurn(source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        val isCanEat = board.analyze(color)
        return if (isCanEat) {
            board.eat(source, destination)
            board.analyze(color)
        } else {
            board.move(source, destination)
            false
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

    private fun convertColumn(columnName: Char): Int =
        if (columnName < 'a' || columnName > 'h') {
            throw ConvertCellException("Column '$columnName' doesn't exists")
        } else {
            columnName.code - 'a'.code + 1
        }

    private fun convertRow(rowName: Char): Int =
        if (rowName < '1' || rowName > '8') {
            throw ConvertCellException("Row '$rowName' doesn't exists")
        } else {
            rowName.code - '1'.code + 1
        }

    override fun toString(): String =
        name
}
