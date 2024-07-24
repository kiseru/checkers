package ru.kiseru.checkers.domain.service

import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.utils.Color

interface BoardService {

    fun makeTurn(board: Board, userColor: Color, from: String, to: String): Boolean

    fun waitNewVersion(board: Board, version: Int)
}

