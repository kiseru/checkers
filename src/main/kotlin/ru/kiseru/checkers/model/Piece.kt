package ru.kiseru.checkers.model

import kotlin.math.abs

abstract class Piece(
    val color: Color,
) {

    var isCanEat = false

    abstract val type: PieceType

    abstract fun analyzeAbilityOfMove(board: Board, source: Pair<Int, Int>)

    abstract fun move(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>)

    abstract fun analyzeAbilityOfEat(board: Board, source: Pair<Int, Int>)

    abstract fun eat(board: Board, source: Pair<Int, Int>, destination: Pair<Int, Int>)

    fun diff(source: Pair<Int, Int>, destination: Pair<Int, Int>): Int =
        if (abs(source.second - destination.second) == abs(source.first - destination.first)) {
            abs(source.second - destination.second)
        } else {
            -1
        }

    fun isPieceEatable(source: Pair<Int, Int>): Boolean =
        source.first in 2..7 && source.second in 2..7
}
