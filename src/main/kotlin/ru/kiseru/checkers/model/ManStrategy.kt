package ru.kiseru.checkers.model

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import ru.kiseru.checkers.error.ChessError
import ru.kiseru.checkers.exception.CannotMoveException
import ru.kiseru.checkers.exception.CellNotFoundException
import ru.kiseru.checkers.exception.MustEatException
import ru.kiseru.checkers.utils.getCellCaption
import ru.kiseru.checkers.utils.isCoordinateExists
import ru.kiseru.checkers.utils.isCoordinatesExists
import kotlin.math.abs

object ManStrategy : PieceStrategy {

    override val type: PieceType = PieceType.MAN

    override fun analyzeAbilityOfMove(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean {
        val nextRow = getNextRow(piece.color, source)
        if (!isCoordinateExists(nextRow)) {
            return false
        }

        return sequenceOf(source.second - 1, source.second + 1)
            .filter { isCoordinateExists(it) }
            .any { isAbleToMoveTo(board, piece, source, nextRow to it) }
    }

    private fun getNextRow(color: Color, source: Pair<Int, Int>): Int =
        if (color == Color.WHITE) {
            source.first + 1
        } else {
            source.first - 1
        }

    override fun analyzeAbilityOfEat(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean =
        sequenceOf(source.first - 2, source.first + 2)
            .filter { isCoordinateExists(it) }
            .flatMap { nextRow ->
                sequenceOf(source.second - 2, source.second + 2)
                    .filter { isCoordinateExists(it) }
                    .map { nextRow to it }
            }
            .any { isAbleToEatTo(board, piece, source, it) }

    private fun isAbleToMoveTo(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Boolean {
        if (board.getPiece(destination) != null
            || isEnemyNear(board, piece, source) ||
            diff(source, destination) != 1
        ) {
            return false
        }

        val nextRow = getNextRow(piece.color, source)
        return sequenceOf(source.second - 1, source.second + 1)
            .filter { isCoordinateExists(it) }
            .map { nextRow to it }
            .any { it == destination }
    }

    private fun isEnemyNear(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>
    ): Boolean {
        return sequenceOf(
            source.first + 1 to source.second + 1,
            source.first + 1 to source.second - 1,
            source.first - 1 to source.second - 1,
            source.first - 1 to source.second + 1,
        )
            .filter { isCoordinatesExists(it) }
            .any { canEatEnemy(board, piece, it) }
    }

    private fun canEatEnemy(board: Board, piece: Piece, destination: Pair<Int, Int>): Boolean {
        val anotherPiece = board.getPiece(destination) ?: return false
        return anotherPiece.color != piece.color && isPieceEatable(destination)
    }

    private fun isPieceEatable(source: Pair<Int, Int>): Boolean =
        source.first in 2..7 && source.second in 2..7

    private fun isAbleToEatTo(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Boolean {
        if (diff(source, destination) != 2 || board.getPiece(destination) != null) {
            return false
        } else {
            val (rowToFind, columnToFind) = between(source, destination)
            return board.board[rowToFind - 1][columnToFind - 1]?.let { it.color != piece.color } ?: false
        }
    }

    private fun diff(source: Pair<Int, Int>, destination: Pair<Int, Int>): Int =
        if (abs(source.second - destination.second) == abs(source.first - destination.first)) {
            abs(source.second - destination.second)
        } else {
            -1
        }

    override fun move(board: Board, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>) {
        if (piece.isCanEat) {
            throw MustEatException()
        }

        if (!piece.isCanMove) {
            throw CannotMoveException(source, destination)
        }

        if (!isAbleToMoveTo(board, piece, source, destination)) {
            throw CannotMoveException(source, destination)
        }

        board.board[source.first - 1][source.second - 1] = null
        updatePiece(board, piece, destination)
    }

    override fun eat(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>
    ): Either<ChessError.CannotEat, Unit> =
        either {
            ensure(isAbleToEatTo(board, piece, source, destination)) {
                ChessError.CannotEat(getCellCaption(source), getCellCaption(destination))
            }

            ensure(piece.isCanEat && isAbleToEatTo(board, piece, source, destination)) {
                ChessError.CannotEat(getCellCaption(source), getCellCaption(destination))
            }

            board.board[source.first - 1][source.second - 1] = null
            val (rowToFind, columnToFind) = between(source, destination)
            board.board[rowToFind - 1][columnToFind - 1] = null
            updatePiece(board, piece, destination)
        }


    private fun between(source: Pair<Int, Int>, destination: Pair<Int, Int>): Pair<Int, Int> =
        if (isCoordinatesExists(destination)) {
            val columnToFind = (source.second + destination.second) / 2
            val rowToFind = (source.first + destination.first) / 2
            rowToFind to columnToFind
        } else {
            throw CellNotFoundException(destination.first, destination.second)
        }

    private fun updatePiece(board: Board, piece: Piece, target: Pair<Int, Int>) {
        board.board[target.first - 1][target.second - 1] =
            if (piece.color == Color.WHITE && target.first == 8 || piece.color == Color.BLACK && target.first == 1) {
                Piece(piece.color, KingStrategy)
            } else {
                piece
            }
    }
}
