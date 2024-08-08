package ru.kiseru.checkers.initializer.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.initializer.BoardInitializer
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.ManStrategy
import ru.kiseru.checkers.model.Piece

@Component
class CheckerBoardInitializer : BoardInitializer {

    override fun initialize(board: Board) {
        initWhitePieces(board)
        initBlackPieces(board)
        board.analyzeAbilities()
    }

    private fun initWhitePieces(board: Board) =
        (0..2).asSequence()
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (row, column) -> (row + column) % 2 == 0 }
            .forEach { (row, column) -> board.board[row][column] = Piece(Color.WHITE, ManStrategy) }

    fun initBlackPieces(board: Board) =
        (5..7).asSequence()
            .flatMap { row ->
                (0..7).asSequence()
                    .map { column -> row to column }
            }
            .filter { (row, column) -> (row + column) % 2 == 0 }
            .forEach { (row, column) -> board.board[row][column] = Piece(Color.BLACK, ManStrategy) }
}
