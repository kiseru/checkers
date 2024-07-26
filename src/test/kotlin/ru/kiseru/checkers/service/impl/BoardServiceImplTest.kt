package ru.kiseru.checkers.service.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import java.util.UUID

class BoardServiceImplTest {

    private val underTest = BoardServiceImpl()

    @Test
    fun `isGameFinished test while there are pieces on the board`() {
        // when
        val board = Board(UUID.randomUUID())
        val actual = underTest.isGameFinished(board)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isGameFinished test while there are only white pieces on the board`() {
        // given
        val board = Board(UUID.randomUUID())
        for (i in board.board.indices) {
            for (j in board.board[i].indices) {
                if (board.board[i][j]?.color == Color.BLACK) {
                    board.board[i][j] = null
                }
            }
        }

        // when
        val actual = underTest.isGameFinished(board)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isGameFinished test while there are only black pieces on the board`() {
        // given
        val board = Board(UUID.randomUUID())
        for (i in board.board.indices) {
            for (j in board.board[i].indices) {
                if (board.board[i][j]?.color == Color.WHITE) {
                    board.board[i][j] = null
                }
            }
        }

        // when
        val actual = underTest.isGameFinished(board)

        // then
        assertThat(actual).isTrue()
    }
}
