package ru.kiseru.checkers.converter

interface CellNotationConverter {

    fun convert(turnNotation: String): Pair<Int, Int>
}

