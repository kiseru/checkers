package ru.kiseru.checkers.model

class Room(
    val id: Int,
    val board: Board,
) {

    var whitePlayer: User? = null

    var blackPlayer: User? = null

    var turn: Color = Color.WHITE
}
