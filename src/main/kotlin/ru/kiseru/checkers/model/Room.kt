package ru.kiseru.checkers.model

import java.util.UUID

class Room(
    val id: UUID,
    val name: String,
    val board: Board,
) {

    var whitePlayer: User? = null

    var blackPlayer: User? = null

    var turn: Color = Color.WHITE
}
