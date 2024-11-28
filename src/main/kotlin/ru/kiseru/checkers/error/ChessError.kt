package ru.kiseru.checkers.error

sealed interface ChessError {
    data class CannotEat(val source: Pair<Int, Int>, val destination: Pair<Int, Int>) : ChessError
    data class CannotMove(val source: Pair<Int, Int>, val destination: Pair<Int, Int>) : ChessError
    data class EmptyCell(val cell: Pair<Int, Int>) : ChessError
    data class BusyCell(val cell: Pair<Int, Int>) : ChessError
    data object MustEat : ChessError
}
