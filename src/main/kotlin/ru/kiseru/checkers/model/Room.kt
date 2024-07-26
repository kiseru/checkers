package ru.kiseru.checkers.model

class Room(
    val id: Int,
    val board: Board,
) {

    var firstPlayer: User? = null

    var secondPlayer: User? = null

    var turn: User? = null
}
