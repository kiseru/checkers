package ru.kiseru.checkers.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CellNotFoundException(row: Int, column: Int)
    : RuntimeException("Cell not found on row $row and column $column")
