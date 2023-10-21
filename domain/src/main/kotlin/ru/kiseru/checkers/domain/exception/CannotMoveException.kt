package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.board.Cell

class CannotMoveException(sourceCell: Cell, destinationCell: Cell) :
    RuntimeException("Can't move from $sourceCell to $destinationCell")
