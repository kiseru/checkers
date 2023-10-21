package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.board.Cell

class CellIsEmptyException(cell: Cell) : RuntimeException("Cell $cell doesn't have any piece")
