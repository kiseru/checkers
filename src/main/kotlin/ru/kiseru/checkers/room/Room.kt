package ru.kiseru.checkers.room

import ru.kiseru.checkers.board.Board
import ru.kiseru.checkers.user.User

class Room(
    val id: Int,
    val board: Board,
) {

    var firstPlayer: User? = null

    var secondPlayer: User? = null

    var turn: User? = null
}
