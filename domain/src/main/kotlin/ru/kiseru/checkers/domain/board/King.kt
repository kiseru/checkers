package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.MustEatException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.isCoordinatesExists
import kotlin.math.abs
import kotlin.math.sign

class King(color: Color) : Piece(color) {

    override fun analyzeAbilityOfMove() {
        isCanMove = generateSequence(1) { it + 1 }
            .take(7)
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .filter { isCoordinatesExists(cell.row + it.first, cell.column + it.second) }
            .any { isAbleToMoveTo(cell.getNear(it.first, it.second)) }
    }

    override fun move(destinationCell: Cell) {
        if (isCanEat) {
            throw MustEatException()
        }

        if (!isCanMove || !isAbleToMoveTo(destinationCell)) {
            throw CannotMoveException(cell, destinationCell)
        }

        cell.piece = null
        destinationCell.piece = this
    }

    override fun isAbleToMoveTo(destinationCell: Cell): Boolean {
        if (cell.diff(destinationCell) == -1) {
            return false
        }

        if (destinationCell.piece != null) {
            return false
        }

        if (isOnOtherDiagonal(destinationCell)) {
            return false
        }

        val deltaColumn = destinationCell.column - cell.column
        val deltaRow = destinationCell.row - cell.row

        val deltaColumnSign = sign(deltaColumn.toDouble()).toInt()
        val deltaRowSign = sign(deltaRow.toDouble()).toInt()

        val board = cell.board
        var i = 1
        while (true) {
            val currentRow = cell.row + deltaRowSign * i
            val currentColumn = cell.column + deltaColumnSign * i

            if (currentRow == destinationCell.row && currentColumn == destinationCell.column) {
                return true
            }

            val currentCell = board.getCell(currentRow, currentColumn)
            if (currentCell.piece != null) {
                return false
            }

            i++
        }
    }

    override fun analyzeAbilityOfEat() {
        isCanEat = generateSequence(2) { it + 1 }
            .take(6)
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .filter { isCoordinatesExists(cell.row + it.first, cell.column + it.second) }
            .any { isAbleToEatTo(cell.getNear(it.first, it.second)) }
    }

    override fun eat(destinationCell: Cell) {
        if (!isCanEat || !isAbleToEatTo(destinationCell)) {
            throw CannotEatException(cell, destinationCell)
        }

        val sacrificedPiece = getSacrificedPiece(destinationCell) ?: throw CannotEatException(cell, destinationCell)
        val sacrificedPieceCell = sacrificedPiece.cell
        cell.piece = null
        sacrificedPieceCell.piece = null
        destinationCell.piece = this
    }

    override fun isAbleToEatTo(destinationCell: Cell): Boolean {
        if (destinationCell.piece != null) {
            return false
        }

        if (isOnOtherDiagonal(destinationCell)) {
            return false
        }

        if (cell.diff(destinationCell) < 2) {
            return false
        }

        val deltaColumn = destinationCell.column - cell.column
        val deltaRow = destinationCell.row - cell.row

        val deltaColumnSign = sign(deltaColumn.toFloat()).toInt()
        val deltaRowSign = sign(deltaRow.toFloat()).toInt()

        var enemyPieceCount = 0
        var i = 1
        while (true) {
            val currentRow = cell.row + deltaRowSign * i
            val currentColumn = cell.column + deltaColumnSign * i
            if (currentColumn == destinationCell.column && currentRow == destinationCell.row) {
                return enemyPieceCount == 1
            }

            val currentCell = cell.board.getCell(currentRow, currentColumn)
            val piece = currentCell.piece
            if (piece == null) {
                i++
                continue
            }

            if (piece.color == color) {
                return false
            }

            enemyPieceCount++
            i++
        }
    }

    private fun isOnOtherDiagonal(destinationCell: Cell): Boolean {
        val deltaColumn = destinationCell.column - cell.column
        val deltaRow = destinationCell.row - cell.row
        val k = deltaColumn.toFloat() / deltaRow
        return abs(k) != 1f
    }

    private fun getSacrificedPiece(destinationCell: Cell): Piece? {
        val destinationRow = destinationCell.row
        val destinationColumn = destinationCell.column
        val sourceRow = cell.row
        val sourceColumn = cell.column

        val signRow = sign((destinationCell.row - cell.row).toFloat()).toInt()
        val signCol = sign((destinationCell.column - cell.column).toFloat()).toInt()

        var i = 1
        while (true) {
            val currentRow = sourceRow + signRow * i
            val currentColumn = sourceColumn + signCol * i
            if (currentRow == destinationRow && currentColumn == destinationColumn) {
                return null
            }

            val currentCell = cell.board.getCell(currentRow, currentColumn)
            val piece = currentCell.piece
            if (piece == null) {
                i++
                continue
            }

            return if (piece.color == color) {
                null
            } else {
                piece
            }
        }
    }

    override fun isMan(): Boolean =
        false

    override fun isKing(): Boolean =
        true

    override fun getCssClass(): String =
        if (color == Color.WHITE) {
            "piece white-king"
        } else {
            "piece black-king"
        }

    override fun toString(): String =
        if (color == Color.BLACK) {
            "&"
        } else {
            "#"
        }
}
