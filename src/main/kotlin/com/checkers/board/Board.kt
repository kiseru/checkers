package com.checkers.board

import com.checkers.exception.*
import com.checkers.user.User
import com.checkers.utils.BoardUtils
import com.checkers.utils.Color

class Board {

    val board: Array<Array<Cell>> = Array(SIZE_OF_BOARD) { row ->
        Array(SIZE_OF_BOARD) { column ->
            Cell(row + 1, column + 1, this)
        }
    }

    private var whitePieces = 12

    private var blackPieces = 12

    init {
        initWhitePieces()
        initBlackPieces()
        analyzeAbilities()
    }

    private fun initWhitePieces() =
        (0..2).asSequence()
            .flatMap { board[it].asSequence() }
            .filter { it.color == Color.BLACK }
            .forEach { it.piece = Man(Color.WHITE) }

    private fun initBlackPieces() =
        (5..7).asSequence()
            .flatMap { board[it].asSequence() }
            .filter { it.color == Color.BLACK }
            .forEach { it.piece = Man(Color.BLACK) }

    private fun cells(): Sequence<Cell> =
        board.asSequence()
            .flatMap { it.asSequence() }

    private fun pieces(): Sequence<Piece> =
        cells()
            .mapNotNull { it.piece }

    fun getCell(row: Int, column: Int): Cell =
        if (BoardUtils.isCoordinatesExists(row, column)) {
            board[row - 1][column - 1]
        } else {
            throw CellNotFoundException(row, column)
        }

    fun move(sourceCell: Cell, destinationCell: Cell) {
        val piece = sourceCell.piece
            ?: throw CellIsEmptyException(sourceCell)

        if (destinationCell.piece != null) {
            throw CellIsBusyException(destinationCell)
        }

        if (piece.isAbleToMoveTo(destinationCell)) {
            piece.move(destinationCell)
        } else {
            throw CannotMoveException(sourceCell, destinationCell)
        }
    }

    fun eat(sourceCell: Cell, destinationCell: Cell) {
        val piece = sourceCell.piece
            ?: throw CellIsEmptyException(sourceCell)

        if (destinationCell.piece != null) {
            throw CellIsBusyException(destinationCell)
        }

        if (piece.isAbleToEatTo(destinationCell)) {
            piece.eat(destinationCell)
        } else {
            throw CannotEatException(sourceCell, destinationCell)
        }
    }

    fun analyze(user: User) {
        val userColor = user.color

        analyzeAbilities()

        user.isCanEat = pieces()
            .any { it.color == userColor && it.isCanEat }
    }

    private fun analyzeAbilities() =
        pieces()
            .forEach {
                it.analyzeAbilityOfMove()
                it.analyzeAbilityOfEat()
            }

    fun isGaming(): Boolean =
        whitePieces != 0 && blackPieces != 0

    fun decrementWhitePieceCount() {
        if (whitePieces > 0) {
            whitePieces--
        }
    }

    fun decrementBlackPieceCount() {
        if (blackPieces > 0) {
            blackPieces--
        }
    }

    companion object {
        private const val SIZE_OF_BOARD = 8
    }
}