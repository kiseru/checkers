package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.utils.getCellCaption

class CannotMoveException(source: Pair<Int, Int>, destination: Pair<Int, Int>) :
    RuntimeException("Can't move from ${getCellCaption(source)} to ${getCellCaption(destination)}")
