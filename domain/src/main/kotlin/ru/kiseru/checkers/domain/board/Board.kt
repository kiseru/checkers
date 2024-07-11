package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.exception.CellIsBusyException
import ru.kiseru.checkers.domain.exception.CellIsEmptyException
import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.domain.utils.isCoordinatesExists
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Board {

    val board: Array<Array<Piece?>> = Array(SIZE_OF_BOARD) { arrayOfNulls(SIZE_OF_BOARD) }

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
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (it.first + it.second) % 2 == 0 }
            .forEach { board[it.first][it.second] = Man(Color.WHITE, it.first + 1, it.second + 1, this) }

    private fun initBlackPieces() =
        (5..7).asSequence()
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (it.first + it.second) % 2 == 0 }
            .forEach { board[it.first][it.second] = Man(Color.BLACK, it.first + 1, it.second + 1, this) }

    fun pieces(): Sequence<Piece> =
        board.asSequence()
            .flatMap { it.asSequence() }
            .filterNotNull()

    fun move(source: Pair<Int, Int>, destination: Pair<Int, Int>) =
        lock.withLock {
            val piece = getPiece(source)
                ?: throw CellIsEmptyException(source.first, source.second)

            if (getPiece(destination) != null) {
                throw CellIsBusyException(destination.first, destination.second)
            }

            piece.move(destination)
            updateVersion()
            condition.signalAll()
        }

    fun eat(source: Pair<Int, Int>, destination: Pair<Int, Int>) =
        lock.withLock {
            val piece = getPiece(source)
                ?: throw CellIsEmptyException(source.first, source.second)

            if (getPiece(destination) != null) {
                throw CellIsBusyException(destination.first, destination.second)
            }

            piece.eat(destination)
            updateVersion()
            condition.signalAll()
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
