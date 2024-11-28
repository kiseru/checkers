package ru.kiseru.checkers.model

import arrow.core.Either
import ru.kiseru.checkers.error.ChessError

interface PieceStrategy {

    val type: PieceType

    fun analyzeAbilityOfMove(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean

    fun analyzeAbilityOfEat(board: Board, piece: Piece, source: Pair<Int, Int>): Boolean

    fun move(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>,
    ): Either<ChessError.CannotMove, Unit>

    fun eat(
        board: Board,
        piece: Piece,
        source: Pair<Int, Int>,
        destination: Pair<Int, Int>,
    ): Either<ChessError.CannotEat, Unit>
}
