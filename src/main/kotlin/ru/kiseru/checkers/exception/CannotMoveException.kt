package ru.kiseru.checkers.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import ru.kiseru.checkers.board.Cell

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CannotMoveException(sourceCell: Cell, destinationCell: Cell) :
    RuntimeException("Can't move from $sourceCell to $destinationCell")
