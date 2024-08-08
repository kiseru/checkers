package ru.kiseru.checkers.model

open class Piece(
    val color: Color,
    val pieceStrategy: PieceStrategy,
) {

    var isCanEat = false

    var isCanMove = false
}
