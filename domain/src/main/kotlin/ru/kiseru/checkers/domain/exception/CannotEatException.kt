package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.board.Cell

class CannotEatException(sourceCell: Cell, destinationCell: Cell) :
    RuntimeException("Can't eat from $sourceCell to $destinationCell")
