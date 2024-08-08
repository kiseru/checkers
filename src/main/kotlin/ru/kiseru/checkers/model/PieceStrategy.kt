package ru.kiseru.checkers.model

interface PieceStrategy {

    val type: PieceType

    fun analyzeAbilityOfMove(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean

    fun analyzeAbilityOfEat(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean

    fun move(board: Board, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>)

    fun eat(board: Board, piece: Piece, source: Pair<Int, Int>, destination: Pair<Int, Int>)
}
