package ru.kiseru.checkers.domain.board

import ru.kiseru.checkers.domain.utils.Color
import kotlin.math.abs

abstract class Piece(
    val color: Color,
    var row: Int,
    var column: Int,
) {

    var isCanEat = false

    abstract val type: PieceType

    abstract fun analyzeAbilityOfMove()

    abstract fun move(destination: Pair<Int, Int>)

    abstract fun analyzeAbilityOfEat()

    abstract fun eat(destination: Pair<Int, Int>)

    fun diff(another: Pair<Int, Int>): Int =
        if (abs(column - another.second) == abs(row - another.first)) {
            abs(column - another.second)
        } else {
            -1
        }

    fun isPieceEatable(): Boolean =
        row != 1 && row != 8 && column != 1 && column != 8
}
