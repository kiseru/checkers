package ru.kiseru.checkers.domain.room

import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.user.User

class Room(
    val id: Int,
    val board: Board,
) {

    var firstPlayer: User? = null

    var secondPlayer: User? = null

    var turn: User? = null
}
