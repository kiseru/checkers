package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.utils.getCellCaption

class CellIsBusyException(row: Int, column: Int) :
    RuntimeException("Cell ${getCellCaption(row, column)} already has the piece")
