package ru.kiseru.checkers.exception

import ru.kiseru.checkers.utils.getCellCaption

class CannotEatException(source: Pair<Int, Int>, destination: Pair<Int, Int>) :
    RuntimeException("Can't eat from ${getCellCaption(source)} to ${getCellCaption(destination)}")
