package ru.kiseru.checkers.converter

import arrow.core.Either

interface CellNotationConverter {

    fun convert(turnNotation: String): Either<String, Pair<Int, Int>>
}

