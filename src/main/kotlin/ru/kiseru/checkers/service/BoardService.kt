package ru.kiseru.checkers.service

import arrow.core.Either
import ru.kiseru.checkers.error.ChessError
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color

interface BoardService {

    fun makeTurn(
        board: Board,
        userColor: Color,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>,
    ): Either<ChessError, Boolean>

    fun waitNewVersion(board: Board, version: Int)

    /**
     * Возвращает `true`, если игра окончена.
     *
     * @param board доска с шашками
     */
    fun isGameFinished(board: Board): Boolean
}

