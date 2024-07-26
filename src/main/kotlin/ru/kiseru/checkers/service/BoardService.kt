package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color

interface BoardService {

    fun makeTurn(board: Board, userColor: Color, from: String, to: String): Boolean

    fun waitNewVersion(board: Board, version: Int)

    /**
     * Возвращает `true`, если игра окончена.
     *
     * @param board доска с шашками
     */
    fun isGameFinished(board: Board): Boolean
}

