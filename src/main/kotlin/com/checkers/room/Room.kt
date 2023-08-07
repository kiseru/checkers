package com.checkers.room

import com.checkers.board.Board
import com.checkers.user.User

class Room(
    val id: Int,
    val board: Board,
) {

    var firstPlayer: User? = null

    var secondPlayer: User? = null

    var turn: User? = null
}
