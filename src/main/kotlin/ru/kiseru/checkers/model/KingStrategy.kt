package ru.kiseru.checkers.model

import ru.kiseru.checkers.exception.CannotEatException
import ru.kiseru.checkers.exception.CannotMoveException
import ru.kiseru.checkers.exception.MustEatException
import ru.kiseru.checkers.utils.isCoordinatesExists
import kotlin.math.abs
import kotlin.math.sign

object KingStrategy : PieceStrategy {

    override val type: PieceType = PieceType.KING

    override fun analyzeAbilityOfMove(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean =
        generateSequence(1) { it + 1 }
            .take(7)
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .filter { isCoordinatesExists(source.first + it.first, source.second + it.second) }
            .any { isAbleToMoveTo(board, source, source.first + it.first to source.second + it.second) }

    override fun move(board: Board, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>) {
        if (piece.isCanEat) {
            throw MustEatException()
        }

        if (!piece.isCanMove || !isAbleToMoveTo(board, source, destination)) {
            throw CannotMoveException(source, destination)
        }

        board.board[source.first - 1][source.second - 1] = null
        board.board[destination.first - 1][destination.second - 1] = piece
    }

    private fun isAbleToMoveTo(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        if (diff(source, destination) == -1) {
            return false
        }

        if (board.getPiece(destination) != null) {
            return false
        }

        if (isOnOtherDiagonal(source, destination)) {
            return false
        }

        val deltaColumn = destination.second - source.second
        val deltaRow = destination.first - source.first

        val deltaColumnSign = sign(deltaColumn.toDouble()).toInt()
        val deltaRowSign = sign(deltaRow.toDouble()).toInt()

        var i = 1
        while (true) {
            val currentRow = source.first + deltaRowSign * i
            val currentColumn = source.second + deltaColumnSign * i

            if (currentRow == destination.first && currentColumn == destination.second) {
                return true
            }

            if (board.getPiece(currentRow to currentColumn) != null) {
                return false
            }

            i++
        }
    }

    override fun analyzeAbilityOfEat(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>
    ): Boolean {
        return generateSequence(2) { it + 1 }
            .take(6)
            .flatMap { sequenceOf(it to it, it to -it, -it to -it, -it to it) }
            .filter { isCoordinatesExists(source.first + it.first, source.second + it.second) }
            .any { isAbleToEatTo(board, piece, source, source.first + it.first to source.second + it.second) }
    }

    override fun eat(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ) {
        if (!piece.isCanEat || !isAbleToEatTo(board, piece, source, destination)) {
            throw CannotEatException(source, destination)
        }

        val sacrificedPieceLocation = getSacrificedPieceLocation(board, piece, source, destination)
        board.board[sacrificedPieceLocation.first - 1][sacrificedPieceLocation.second - 1] = null
        board.board[source.first - 1][source.second - 1] = null
        board.board[destination.first - 1][destination.second - 1] = piece
    }

    private fun isAbleToEatTo(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Boolean {
        if (board.getPiece(destination) != null) {
            return false
        }

        if (isOnOtherDiagonal(source, destination)) {
            return false
        }

        if (diff(source, destination) < 2) {
            return false
        }

        val deltaColumn = destination.second - source.second
        val deltaRow = destination.first - source.first

        val deltaColumnSign = sign(deltaColumn.toFloat()).toInt()
        val deltaRowSign = sign(deltaRow.toFloat()).toInt()

        var enemyPieceCount = 0
        var i = 1
        while (true) {
            val currentRow = source.first + deltaRowSign * i
            val currentColumn = source.second + deltaColumnSign * i
            if (currentColumn == destination.second && currentRow == destination.first) {
                return enemyPieceCount == 1
            }

            val anotherPiece = board.getPiece(currentRow to currentColumn)
            if (anotherPiece == null) {
                i++
                continue
            }

            if (anotherPiece.color == piece.color) {
                return false
            }

            enemyPieceCount++
            i++
        }
    }


    private fun isOnOtherDiagonal(source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        val deltaColumn = destination.second - source.second
        val deltaRow = destination.first - source.first
        val k = deltaColumn.toFloat() / deltaRow
        return abs(k) != 1f
    }

    private fun diff(source: Pair<Int, Int>, destination: Pair<Int, Int>): Int =
        if (abs(source.second - destination.second) == abs(source.first - destination.first)) {
            abs(source.second - destination.second)
        } else {
            -1
        }

    private fun getSacrificedPieceLocation(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Pair<Int, Int> {
        val signRow = sign((destination.first - source.first).toFloat()).toInt()
        val signCol = sign((destination.second - source.second).toFloat()).toInt()

        var i = 1
        while (true) {
            val currentRow = source.first + signRow * i
            val currentColumn = source.second + signCol * i
            if (currentRow == destination.first && currentColumn == destination.second) {
                throw CannotEatException(source, destination)
            }

            val anotherPiece = board.getPiece(currentRow to currentColumn)
            if (anotherPiece == null) {
                i++
                continue
            }

            return if (anotherPiece.color == piece.color) {
                throw CannotEatException(source, destination)
            } else {
                currentRow to currentColumn
            }
        }
    }
}
