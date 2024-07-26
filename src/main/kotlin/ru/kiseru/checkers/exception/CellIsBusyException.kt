package ru.kiseru.checkers.exception

import ru.kiseru.checkers.utils.getCellCaption

class CellIsBusyException(row: Int, column: Int) :
    RuntimeException("Cell ${getCellCaption(row, column)} already has the piece")
