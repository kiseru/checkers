package com.checkers.exception

import com.checkers.board.Cell
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CellIsEmptyException(cell: Cell) : RuntimeException("Cell $cell doesn't have any piece")
