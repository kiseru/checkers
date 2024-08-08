package ru.kiseru.checkers.service.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.ManStrategy
import ru.kiseru.checkers.model.Piece
import java.util.UUID

class BoardServiceImplTest {

    private val underTest = BoardServiceImpl()

    @Test
    fun `isGameFinished test while there are pieces on the board`() {
        // given
        val board = Board(UUID.randomUUID())
        board.board[0][0] = Piece(Color.WHITE, ManStrategy)
        board.board[1][1] = Piece(Color.BLACK, ManStrategy)

        // when
        val actual = underTest.isGameFinished(board)

        // then
        assertThat(actual).isFalse()
    }

    @ParameterizedTest
    @EnumSource(Color::class)
    fun `isGameFinished test when there are one colored pieces on the board`(color: Color) {
        // given
        val board = Board(UUID.randomUUID())
        board.board[0][0] = Piece(color, ManStrategy)

        // when
        val actual = underTest.isGameFinished(board)

        // then
        assertThat(actual).isTrue()
    }
}
