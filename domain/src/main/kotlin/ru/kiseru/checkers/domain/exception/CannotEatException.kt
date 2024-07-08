package ru.kiseru.checkers.domain.exception

import ru.kiseru.checkers.domain.utils.getCellCaption

class CannotEatException(source: Pair<Int, Int>, destination: Pair<Int, Int>) :
    RuntimeException("Can't eat from ${getCellCaption(source)} to ${getCellCaption(destination)}")
