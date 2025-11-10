package ru.kiseru.checkers.converter.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.converter.CellNotationConverter

@Component
class CheckersCellNotationConverter : CellNotationConverter {

    override fun convert(turnNotation: String): Pair<Int, Int> {
        require(turnNotation.length == 2) { "Can't convert '$turnNotation' to cell" }
        val column = convertColumn(turnNotation[0])
        val row = convertRow(turnNotation[1])
        return row to column
    }

    private fun convertColumn(columnName: Char): Int {
        require(columnName in 'a'..'h') { "Column '$columnName' doesn't exists" }
        return columnName.code - 'a'.code + 1
    }

    private fun convertRow(rowName: Char): Int {
        require(rowName in '1'..'8') { "Row '$rowName' doesn't exists" }
        return rowName.code - '1'.code + 1
    }
}
