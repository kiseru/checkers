package ru.kiseru.checkers.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import ru.kiseru.checkers.board.Cell

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CellIsEmptyException(cell: Cell) : RuntimeException("Cell $cell doesn't have any piece")
