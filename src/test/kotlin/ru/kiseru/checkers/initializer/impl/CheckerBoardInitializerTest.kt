package ru.kiseru.checkers.initializer.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import java.util.UUID

class CheckerBoardInitializerTest {

    private val boardSize = 8

    private val underTest = CheckerBoardInitializer()

    @Test
    fun `init test`() {
        // given
        val board = Board(UUID.randomUUID())

        // when
        underTest.initialize(board)

        // then
        assertThat(board.board).isNotNull()
        assertThat(board.board.size).isEqualTo(boardSize)

        board.board.forEach { assertThat(it.size).isEqualTo(boardSize) }

        val pieceCount = board.board.flatMap { it.asSequence() }
            .count { it != null }
        assertThat(pieceCount).isEqualTo(24)

        val whitePieceCount = board.board.flatMap { it.asSequence() }
            .count { it?.color == Color.WHITE }
        assertThat(whitePieceCount).isEqualTo(12)

        val blackPieceCount = board.board.flatMap { it.asSequence() }
            .count { it?.color == Color.BLACK }
        assertThat(blackPieceCount).isEqualTo(12)
    }

    @ParameterizedTest
    @CsvSource(
        "1, 1, WHITE",
        "1, 3, WHITE",
        "1, 5, WHITE",
        "1, 7, WHITE",
        "2, 2, WHITE",
        "2, 4, WHITE",
        "2, 6, WHITE",
        "2, 8, WHITE",
        "3, 1, WHITE",
        "3, 3, WHITE",
        "3, 5, WHITE",
        "3, 7, WHITE",
        "6, 2, BLACK",
        "6, 4, BLACK",
        "6, 6, BLACK",
        "6, 8, BLACK",
        "7, 1, BLACK",
        "7, 3, BLACK",
        "7, 5, BLACK",
        "7, 7, BLACK",
        "8, 2, BLACK",
        "8, 4, BLACK",
        "8, 6, BLACK",
        "8, 8, BLACK",
    )
    fun `init pieces test`(row: Int, column: Int, expected: Color) {
        // given
        val board = Board(UUID.randomUUID())

        // when
        underTest.initialize(board)

        // then
        val piece = board.board[row - 1][column - 1]
        assertThat(piece).isNotNull
        assertThat(piece?.color).isEqualTo(expected)
    }
}
