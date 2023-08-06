package com.checkers.exception

import com.checkers.board.Cell
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CannotMoveException(sourceCell: Cell, destinationCell: Cell) :
    RuntimeException("Can't move from $sourceCell to $destinationCell")
