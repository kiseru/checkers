package com.checkers.user

import com.checkers.board.Board
import com.checkers.board.Cell
import com.checkers.exception.CellException
import com.checkers.exception.ConvertCellException
import com.checkers.exception.PieceException
import com.checkers.utils.BoardUtils
import com.checkers.utils.Color

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

        val column = BoardUtils.convertColumn(cell[0])
        val row = BoardUtils.convertRow(cell[1])
        return board.getCell(row, column)
    }

    override fun toString(): String =
        name
}
