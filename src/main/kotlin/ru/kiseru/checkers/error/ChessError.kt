package ru.kiseru.checkers.error

import ru.kiseru.checkers.model.Color

sealed interface ChessError {
    data class CannotEat(val source: Pair<Int, Int>, val destination: Pair<Int, Int>) : ChessError
    data class CannotMove(val source: Pair<Int, Int>, val destination: Pair<Int, Int>) : ChessError
    data class EmptyCell(val cell: Pair<Int, Int>) : ChessError
    data class BusyCell(val cell: Pair<Int, Int>) : ChessError
    data object MustEat : ChessError
    data class PieceOwner(val userColor: Color) : ChessError
}
