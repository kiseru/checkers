package ru.kiseru.checkers.exception

import ru.kiseru.checkers.utils.getCellCaption

class CannotMoveException(source: Pair<Int, Int>, destination: Pair<Int, Int>) :
    RuntimeException("Can't move from ${getCellCaption(source)} to ${getCellCaption(destination)}")
