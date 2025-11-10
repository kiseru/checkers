package ru.kiseru.checkers.converter.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.converter.CellNotationConverter

@Component
class CheckersCellNotationConverter : CellNotationConverter {

    companion object {
        private const val MIN_COLUMN = 'a'
        private const val MAX_COLUMN = 'h'
        private const val MIN_ROW = '1'
        private const val MAX_ROW = '8'
        private const val ASCII_OFFSET = 1
    }

    override fun convert(turnNotation: String): Pair<Int, Int> {
        require(turnNotation.length == 2) { "Invalid notation length: '$turnNotation'. Expected 2 characters." }

        val columnChar = turnNotation[0]
        val rowChar = turnNotation[1]

        val column = convertColumn(columnChar)
        val row = convertRow(rowChar)

        return row to column
    }

    private fun convertColumn(columnName: Char): Int {
        require(columnName in MIN_COLUMN..MAX_COLUMN) {
            "Invalid column: '$columnName'. Must be between $MIN_COLUMN and $MAX_COLUMN."
        }

        return columnName.code - MIN_COLUMN.code + ASCII_OFFSET
    }

    private fun convertRow(rowName: Char): Int {
        require(rowName in MIN_ROW..MAX_ROW) {
            "Invalid row: '$rowName'. Must be between $MIN_ROW and $MAX_ROW."
        }

        return rowName.code - '1'.code + 1
    }
}
