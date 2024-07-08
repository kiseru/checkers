package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.utils.getCellCaption

class CellIsEmptyException(row: Int, column: Int)
    : RuntimeException("Cell ${getCellCaption(row, column)} doesn't have any piece")
