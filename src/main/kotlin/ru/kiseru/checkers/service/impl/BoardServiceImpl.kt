package ru.kiseru.checkers.service.impl

import arrow.core.Either
import arrow.core.getOrElse
import org.springframework.stereotype.Service
import ru.kiseru.checkers.error.ChessError
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.service.BoardService
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Service
class BoardServiceImpl : BoardService {

    private val locks = ConcurrentHashMap<UUID, Pair<Lock, Condition>>()

    override fun makeTurn(
        board: Board,
        userColor: Color,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>,
    ): Either<ChessError.CannotEat, Boolean> {
        val (lock, condition) = locks.computeIfAbsent(board.id) {
            val lock = ReentrantLock()
            lock to lock.newCondition()
        }

        return lock.withLock {
            board.makeTurn(userColor, source, destination)
                .onRight { condition.signalAll() }
        }
    }

    override fun waitNewVersion(board: Board, version: Int) {
        val (lock, condition) = locks.computeIfAbsent(board.id) {
            val lock = ReentrantLock()
            lock to lock.newCondition()
        }

        lock.withLock {
            if (version == board.version) {
                condition.await()
            }
        }
    }

    override fun isGameFinished(board: Board): Boolean =
        countPiecesByColor(board, Color.WHITE) == 0 || countPiecesByColor(board, Color.BLACK) == 0

    private fun countPiecesByColor(board: Board, color: Color): Int =
        board.pieces()
            .count { it.color == color }
}
