package ru.kiseru.checkers.error

sealed interface ChessError {
    data class CellCaptionIsNotExisted(val row: Int, val column: Int) : ChessError
    data class CannotEat(val sourceCell: String, val destinationCell: String) : ChessError
}
