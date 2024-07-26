package ru.kiseru.checkers.exception

class CellNotFoundException(row: Int, column: Int)
    : RuntimeException("Cell not found on row $row and column $column")
