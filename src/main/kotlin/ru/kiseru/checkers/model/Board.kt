package ru.kiseru.checkers.model

import ru.kiseru.checkers.exception.CellException
import ru.kiseru.checkers.exception.CellIsBusyException
import ru.kiseru.checkers.exception.CellNotFoundException
import ru.kiseru.checkers.exception.PieceException
import ru.kiseru.checkers.utils.getCellCaption
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

    fun makeTurn(userColor: Color, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        val piece = getUserPiece(source, userColor)
        checkForPiece(destination)
        val isCanEat = makeTurn(userColor, piece, source, destination)
        updateVersion()
        return isCanEat
    }

    private fun getUserPiece(source: Pair<Int, Int>, userColor: Color): Piece {
        val piece = getPiece(source) ?: throw CellException("Cell '${getCellCaption(source)}' is empty")
        checkPieceOwner(userColor, piece)
        return piece
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

    private fun makeTurn(userColor: Color, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>): Boolean {
        val isCanEat = analyze(userColor)
        return if (isCanEat) {
            piece.pieceStrategy.eat(this, piece, source, destination)
            analyze(userColor)
        } else {
            piece.pieceStrategy.move(this, piece, source, destination)
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
