package ru.kiseru.checkers.model

import ru.kiseru.checkers.exception.CannotEatException
import ru.kiseru.checkers.exception.CannotMoveException
import ru.kiseru.checkers.exception.CellNotFoundException
import ru.kiseru.checkers.exception.MustEatException
import ru.kiseru.checkers.utils.isCoordinateExists
import ru.kiseru.checkers.utils.isCoordinatesExists

class Man(
    color: Color,
) : Piece(color) {

    override val type: PieceType = PieceType.MAN

    var isCanMove = false

    override fun analyzeAbilityOfMove(board: Board, source: Pair<Int, Int>) {
        val nextRow = getNextRow(source)
        if (!isCoordinateExists(nextRow)) {
            return
        }

        isCanMove = sequenceOf(source.second - 1, source.second + 1)
            .filter { isCoordinateExists(it) }
            .any { isAbleToMoveTo(board, source, nextRow to it) }
    }

    private fun getNextRow(source: Pair<Int, Int>): Int =
        if (color == Color.WHITE) {
            source.first + 1
        } else {
            source.first - 1
        }

    override fun analyzeAbilityOfEat(board: Board, source: Pair<Int, Int>) {
        isCanEat = sequenceOf(source.first - 2, source.first + 2)
            .filter { isCoordinateExists(it) }
            .flatMap { nextRow ->
                sequenceOf(source.second - 2, source.second + 2)
                    .filter { isCoordinateExists(it) }
                    .map { nextRow to it }
            }
            .any { isAbleToEatTo(board, source, it) }
    }

    private fun isAbleToMoveTo(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        if (board.getPiece(destination) != null || isEnemyNear(board, source) || diff(source, destination) != 1) {
            return false
        }

        val nextRow = getNextRow(source)
        return sequenceOf(source.second - 1, source.second + 1)
            .filter { isCoordinateExists(it) }
            .map { nextRow to it }
            .any { it == destination }
    }

    private fun isEnemyNear(board: Board, source: Pair<Int, Int>): Boolean =
        sequenceOf(
            source.first + 1 to source.second + 1,
            source.first + 1 to source.second - 1,
            source.first - 1 to source.second - 1,
            source.first - 1 to source.second + 1,
        )
            .filter { isCoordinatesExists(it) }
            .any { canEatEnemy(board, it) }

    private fun canEatEnemy(board: Board, destination: Pair<Int, Int>): Boolean {
        val piece = board.getPiece(destination) ?: return false
        return piece.color != color && piece.isPieceEatable(destination)
    }

    private fun isAbleToEatTo(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean =
        if (diff(source, destination) != 2 || board.getPiece(destination) != null) {
            false
        } else {
            val (rowToFind, columnToFind) = between(source, destination)
            board.board[rowToFind - 1][columnToFind - 1]?.let { it.color != color } ?: false
        }

    override fun move(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>) {
        if (isCanEat) {
            throw MustEatException()
        }

        if (!isCanMove) {
            throw CannotMoveException(source, destination)
        }

        if (!isAbleToMoveTo(board, source, destination)) {
            throw CannotMoveException(source, destination)
        }

        board.board[source.first - 1][source.second - 1] = null
        updatePiece(board, destination)
    }

    override fun eat(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>) {
        if (!isAbleToEatTo(board, source, destination)) {
            throw CannotEatException(source, destination)
        }

        if (!isCanEat || !isAbleToEatTo(board, source, destination)) {
            throw CannotEatException(source, destination)
        }

        board.board[source.first - 1][source.second - 1] = null
        val (rowToFind, columnToFind) = between(source, destination)
        board.board[rowToFind - 1][columnToFind - 1] = null
        updatePiece(board, destination)
    }

    private fun between(source: Pair<Int, Int>, destination: Pair<Int, Int>): Pair<Int, Int> =
        if (isCoordinatesExists(destination)) {
            val columnToFind = (source.second + destination.second) / 2
            val rowToFind = (source.first + destination.first) / 2
            rowToFind to columnToFind
        } else {
            throw CellNotFoundException(destination.first, destination.second)
        }

    private fun updatePiece(board: Board, target: Pair<Int, Int>) {
        board.board[target.first - 1][target.second - 1] =
            if (color == Color.WHITE && target.first == 8 || color == Color.BLACK && target.first == 1) {
                King(color)
            } else {
                this
            }
    }
}
