package ru.kiseru.checkers.model

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import ru.kiseru.checkers.error.ChessError
import ru.kiseru.checkers.exception.CellIsBusyException
import ru.kiseru.checkers.exception.CellNotFoundException
import ru.kiseru.checkers.exception.PieceException
import ru.kiseru.checkers.utils.isCoordinatesExists
import java.util.UUID

class Board(val id: UUID) {

    val board: Array<Array<Piece?>> = Array(SIZE_OF_BOARD) { arrayOfNulls(SIZE_OF_BOARD) }

    var version = 1
        private set

    fun pieces(): Sequence<Piece> =
        piecesCoordinates()
            .mapNotNull { (row, column) -> board[row - 1][column - 1] }

    fun piecesCoordinates(): Sequence<Pair<Int, Int>> =
        sequence {
            for (row in board.indices) {
                for (column in board.indices) {
                    if (board[row][column] != null) {
                        yield(row + 1 to column + 1)
                    }
                }
            }
        }

    fun makeTurn(
        userColor: Color,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>,
    ): Either<ChessError, Boolean> =
        either {
            val piece = getUserPiece(source, userColor).bind()
            checkForPiece(destination)
            makeTurn(userColor, piece, source, destination)
                .onRight { updateVersion() }
                .bind()
        }

    private fun getUserPiece(source: Pair<Int, Int>, userColor: Color): Either<ChessError.EmptyCell, Piece> =
        either {
            val piece = getPiece(source)
            ensureNotNull(piece) {
                ChessError.EmptyCell(source)
            }
            checkPieceOwner(userColor, piece)
            piece
        }

    private fun checkPieceOwner(userColor: Color, piece: Piece) {
        if (piece.color != userColor) {
            throw PieceException("The piece does not belong to the $userColor user")
        }
    }

    private fun checkForPiece(destination: Pair<Int, Int>) {
        if (getPiece(destination) != null) {
            throw CellIsBusyException(destination.first, destination.second)
        }
    }

    private fun makeTurn(
        userColor: Color,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>,
    ): Either<ChessError, Boolean> =
        either {
            val isCanEat = analyze(userColor)
            if (isCanEat) {
                piece.pieceStrategy.eat(this@Board, piece, source, destination).bind()
                analyze(userColor)
            } else {
                piece.pieceStrategy.move(this@Board, piece, source, destination).bind()
                false
            }
        }

    fun getPiece(coordinates: Pair<Int, Int>): Piece? =
        if (isCoordinatesExists(coordinates)) {
            board[coordinates.first - 1][coordinates.second - 1]
        } else {
            throw CellNotFoundException(coordinates.first, coordinates.second)
        }

    private fun updateVersion() {
        version++
    }

    fun analyze(userColor: Color): Boolean {
        analyzeAbilities()

        return pieces()
            .any { it.color == userColor && it.isCanEat }
    }

    fun analyzeAbilities() =
        piecesCoordinates()
            .forEach {
                val piece = board[it.first - 1][it.second - 1]!!
                piece.isCanMove = piece.pieceStrategy.analyzeAbilityOfMove(this, piece, it)
                piece.isCanEat = piece.pieceStrategy.analyzeAbilityOfEat(this, piece, it)
            }

    companion object {
        private const val SIZE_OF_BOARD = 8
    }
}
