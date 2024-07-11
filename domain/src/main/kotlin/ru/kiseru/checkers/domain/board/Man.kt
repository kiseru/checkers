package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.exception.MustEatException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.isCoordinateExists
import ru.kiseru.checkers.domain.utils.isCoordinatesExists

class Man(
    color: Color,
    row: Int,
    column: Int,
    private val board: Board,
) : Piece(color, row, column) {

    override val type: PieceType = PieceType.MAN

    var isCanMove = false

    override fun analyzeAbilityOfMove() {
        val nextRow = getNextRow()
        if (!isCoordinateExists(nextRow)) {
            return
        }

        isCanMove = sequenceOf(column - 1, column + 1)
            .filter { isCoordinateExists(it) }
            .any { isAbleToMoveTo(nextRow to it) }
    }

    private fun getNextRow(): Int =
        if (color == Color.WHITE) {
            row + 1
        } else {
            row - 1
        }

    override fun analyzeAbilityOfEat() {
        isCanEat = sequenceOf(row - 2, row + 2)
            .filter { isCoordinateExists(it) }
            .flatMap { nextRow ->
                sequenceOf(column - 2, column + 2)
                    .filter { isCoordinateExists(it) }
                    .map { nextRow to it }
            }
            .any { isAbleToEatTo(it) }
    }

    private fun isAbleToMoveTo(destination: Pair<Int, Int>): Boolean {
        if (board.getPiece(destination) != null || isEnemyNear() || diff(destination) != 1) {
            return false
        }

        val nextRow = getNextRow()
        return sequenceOf(column - 1, column + 1)
            .filter { isCoordinateExists(it) }
            .map { nextRow to it }
            .any { it == destination }
    }

    private fun isEnemyNear(): Boolean =
        sequenceOf(
            row + 1 to column + 1,
            row + 1 to column - 1,
            row - 1 to column - 1,
            row - 1 to column + 1,
        )
            .filter { isCoordinatesExists(it) }
            .any { canEatEnemy(it) }

    private fun canEatEnemy(coordinates: Pair<Int, Int>): Boolean {
        val piece = board.getPiece(coordinates) ?: return false
        return piece.color != color && piece.isPieceEatable()
    }

    private fun isAbleToEatTo(destination: Pair<Int, Int>): Boolean =
        if (diff(destination) != 2 || board.getPiece(destination) != null) {
            false
        } else {
            val (rowToFind, columnToFind) = between(destination)
            board.board[rowToFind - 1][columnToFind - 1]?.let { it.color != color } ?: false
        }

    override fun move(destination: Pair<Int, Int>) {
        if (isCanEat) {
            throw MustEatException()
        }

        if (!isCanMove) {
            throw CannotMoveException(row to column, destination)
        }

        if (!isAbleToMoveTo(destination)) {
            throw CannotMoveException(row to column, destination)
        }

        board.board[row - 1][column - 1] = null
        updatePiece(destination)
    }

    override fun eat(destination: Pair<Int, Int>) {
        if (!isAbleToEatTo(destination)) {
            throw CannotEatException(row to column, destination)
        }

        if (!isCanEat || !isAbleToEatTo(destination)) {
            throw CannotEatException(row to column, destination)
        }

        board.board[row - 1][column - 1] = null
        val (rowToFind, columnToFind) = between(destination)
        board.board[rowToFind - 1][columnToFind - 1] = null
        updatePiece(destination)
    }

    private fun between(destination: Pair<Int, Int>): Pair<Int, Int> =
        if (isCoordinatesExists(destination)) {
            val columnToFind = (column + destination.second) / 2
            val rowToFind = (row + destination.first) / 2
            rowToFind to columnToFind
        } else {
            throw CellNotFoundException(destination.first, destination.second)
        }

    private fun updatePiece(target: Pair<Int, Int>) {
        board.board[target.first - 1][target.second - 1] =
            if (color == Color.WHITE && target.first == 8 || color == Color.BLACK && target.first == 1) {
                King(color, target.first, target.second, board)
            } else {
                row = target.first
                column = target.second
                this
            }
    }
}
