package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.board.Cell

class CellIsBusyException(cell: Cell) : RuntimeException("Cell $cell already has the piece")
