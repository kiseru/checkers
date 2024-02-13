package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.CellIsBusyException
import ru.kiseru.checkers.domain.exception.CellIsEmptyException
import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.isCoordinatesExists
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Board {

    val board: Array<Array<Cell>> = Array(SIZE_OF_BOARD) { row ->
        Array(SIZE_OF_BOARD) { column ->
            Cell(row + 1, column + 1, this)
        }
    }

    var version = 1
        private set

    private val lock = ReentrantLock()

    private val condition = lock.newCondition()

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

    fun pieces(): Sequence<Piece> =
        cells()
            .mapNotNull { it.piece }

    fun getCell(row: Int, column: Int): Cell =
        if (isCoordinatesExists(row, column)) {
            board[row - 1][column - 1]
        } else {
            throw CellNotFoundException(row, column)
        }

    fun move(sourceCell: Cell, destinationCell: Cell) =
        lock.withLock {
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

            updateVersion()
            condition.signalAll()
        }

    fun eat(sourceCell: Cell, destinationCell: Cell) =
        lock.withLock {
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

            updateVersion()
            condition.signalAll()
        }

    private fun updateVersion() {
        version++
    }

    fun analyze(userColor: Color): Boolean {
        analyzeAbilities()

        return pieces()
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

    fun waitNewVersion(version: Int) =
        lock.withLock {
            while (version == this.version) {
                condition.await()
            }
        }

    companion object {
        private const val SIZE_OF_BOARD = 8
    }
}
