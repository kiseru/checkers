package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color

interface BoardService {

    fun makeTurn(board: Board, userColor: Color, from: String, to: String): Boolean

    fun waitNewVersion(board: Board, version: Int)
}

