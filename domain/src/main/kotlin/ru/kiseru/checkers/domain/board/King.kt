package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.MustEatException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.isCoordinatesExists
import kotlin.math.abs
import kotlin.math.sign

class King(
    color: Color,
    row: Int,
    column: Int,
    private val board: Board,
) : Piece(color, row, column) {

    override val type: PieceType = PieceType.KING

    var isCanMove = false

    override fun analyzeAbilityOfMove() {
        isCanMove = generateSequence(1) { it + 1 }
            .take(7)
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .filter { isCoordinatesExists(row + it.first, column + it.second) }
            .any { isAbleToMoveTo(row + it.first to column + it.second) }
    }

    override fun move(destination: Pair<Int, Int>) {
        if (isCanEat) {
            throw MustEatException()
        }

        if (!isCanMove || !isAbleToMoveTo(destination)) {
            throw CannotMoveException(row to column, destination)
        }

        board.board[row - 1][column - 1] = null
        board.board[destination.first - 1][destination.second - 1] = this
        row = destination.first
        column = destination.second
    }

    private fun isAbleToMoveTo(destination: Pair<Int, Int>): Boolean {
        if (diff(destination) == -1) {
            return false
        }

        if (board.getPiece(destination) != null) {
            return false
        }

        if (isOnOtherDiagonal(destination)) {
            return false
        }

        val deltaColumn = destination.second - column
        val deltaRow = destination.first - row

        val deltaColumnSign = sign(deltaColumn.toDouble()).toInt()
        val deltaRowSign = sign(deltaRow.toDouble()).toInt()

        var i = 1
        while (true) {
            val currentRow = row + deltaRowSign * i
            val currentColumn = column + deltaColumnSign * i

            if (currentRow == destination.first && currentColumn == destination.second) {
                return true
            }

            if (board.getPiece(currentRow to currentColumn) != null) {
                return false
            }

            i++
        }
    }

    override fun analyzeAbilityOfEat() {
        isCanEat = generateSequence(2) { it + 1 }
            .take(6)
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .filter { isCoordinatesExists(row + it.first, column + it.second) }
            .any { isAbleToEatTo(row + it.first to column + it.second) }
    }

    override fun eat(destination: Pair<Int, Int>) {
        if (!isCanEat || !isAbleToEatTo(destination)) {
            throw CannotEatException(row to column, destination)
        }

        val sacrificedPiece = getSacrificedPiece(destination)
            ?: throw CannotEatException(row to column, destination)
        board.board[sacrificedPiece.row - 1][sacrificedPiece.column - 1] = null
        board.board[row - 1][column - 1] = null
        board.board[destination.first - 1][destination.second - 1] = this
        row = destination.first
        column = destination.second
    }

    private fun isAbleToEatTo(destination: Pair<Int, Int>): Boolean {
        if (board.getPiece(destination) != null) {
            return false
        }

        if (isOnOtherDiagonal(destination)) {
            return false
        }

        if (diff(destination) < 2) {
            return false
        }

        val deltaColumn = destination.second - column
        val deltaRow = destination.first - row

        val deltaColumnSign = sign(deltaColumn.toFloat()).toInt()
        val deltaRowSign = sign(deltaRow.toFloat()).toInt()

        var enemyPieceCount = 0
        var i = 1
        while (true) {
            val currentRow = row + deltaRowSign * i
            val currentColumn = column + deltaColumnSign * i
            if (currentColumn == destination.second && currentRow == destination.first) {
                return enemyPieceCount == 1
            }

            val piece = board.getPiece(currentRow to currentColumn)
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

    private fun isOnOtherDiagonal(destination: Pair<Int, Int>): Boolean {
        val deltaColumn = destination.second - column
        val deltaRow = destination.first - row
        val k = deltaColumn.toFloat() / deltaRow
        return abs(k) != 1f
    }

    private fun getSacrificedPiece(destination: Pair<Int, Int>): Piece? {
        val signRow = sign((destination.first - row).toFloat()).toInt()
        val signCol = sign((destination.second - column).toFloat()).toInt()

        var i = 1
        while (true) {
            val currentRow = row + signRow * i
            val currentColumn = column + signCol * i
            if (currentRow == destination.first && currentColumn == destination.second) {
                return null
            }

            val piece = board.getPiece(currentRow to currentColumn)
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

    override fun toString(): String =
        if (color == Color.BLACK) {
            "&"
        } else {
            "#"
        }
}
