package ru.kiseru.checkers.converter.impl

import arrow.core.Either
import arrow.core.Either.*
import arrow.core.raise.either
import org.springframework.stereotype.Component
import ru.kiseru.checkers.converter.CellNotationConverter

@Component
class CheckersCellNotationConverter : CellNotationConverter {

    override fun convert(turnNotation: String): Either<String, Pair<Int, Int>> =
        if (turnNotation.length == 2) {
            val column = convertColumn(turnNotation[0])
            val row = convertRow(turnNotation[1])
            either { row.bind() to column.bind() }
        } else {
            Left("Can't convert '$turnNotation' to cell")
        }

    private fun convertColumn(columnName: Char): Either<String, Int> =
        if (columnName in 'a'..'h') {
            Right(columnName.code - 'a'.code + 1)
        } else {
            Left("Column '$columnName' doesn't exists")
        }

    private fun convertRow(rowName: Char): Either<String, Int> =
        if (rowName in '1'..'8') {
            Right(rowName.code - '1'.code + 1)
        } else {
            Left("Row '$rowName' doesn't exists")
        }
}
