package ru.kiseru.checkers.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.board.Man
import ru.kiseru.checkers.domain.exception.CellException
import ru.kiseru.checkers.domain.exception.ConvertCellException
import ru.kiseru.checkers.domain.exception.PieceException
import ru.kiseru.checkers.domain.user.User
import ru.kiseru.checkers.domain.utils.Color

@ExtendWith(MockitoExtension::class)
class UserTest {

    private val name = "Some cool name"

    private val underTest = User(name)

    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        board = Board()
        for (cells in board.board) {
            for (i in cells.indices) {
                cells[i] = null
            }
        }
        underTest.board = board
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn test when source cell isn't valid`(from: String) {
        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { underTest.makeTurn(from, "a2") }
    }

    @Test
    fun `makeTurn test when source cell hasn't piece`() {
        // when & then
        assertThatExceptionOfType(CellException::class.java)
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @Test
    fun `makeTurn test when source cell hasn't player piece`() {
        // given
        board.board[1][1] = Man(Color.BLACK, 2, 2, board)

        underTest.color = Color.WHITE

        // when & then
        assertThatExceptionOfType(PieceException::class.java)
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn test when destination cell name isn't valid`(to: String) {
        // given
        board.board[1][1] = Man(Color.WHITE, 2, 2, board)

        underTest.color = Color.WHITE

        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { underTest.makeTurn("b2", to) }
    }

    @Test
    fun `makeTurn test when can move`() {
        // given
        val piece = Man(Color.WHITE, 4, 4, board)
        board.board[3][3] = piece

        underTest.color = Color.WHITE

        // when
        val actual = underTest.makeTurn("d4", "e5")

        // then
        assertThat(actual).isFalse()
        assertThat(board.board[3][3]).isNull()
        assertThat(board.board[4][4]).isSameAs(piece)
    }

    @Test
    fun `makeTurn test when can eat`() {
        // given
        val piece = Man(Color.WHITE, 4, 4, board)
        board.board[3][3] = piece

        board.board[4][4] = Man(Color.BLACK, 5, 5, board)

        underTest.color = Color.WHITE

        // when
        val actual = underTest.makeTurn("d4", "f6")

        // then
        assertThat(actual).isFalse()
        assertThat(board.board[3][3]).isNull()
        assertThat(board.board[4][4]).isNull()
        assertThat(board.board[5][5]).isSameAs(piece)
    }

    @Test
    fun `makeTurn test when can eat two pieces`() {
        // given
        val piece = Man(Color.WHITE, 4, 4, board)
        board.board[3][3] = piece

        board.board[4][4] = Man(Color.BLACK, 5, 5, board)
        board.board[6][6] = Man(Color.BLACK, 5, 5, board)

        underTest.color = Color.WHITE

        // when
        val actual = underTest.makeTurn("d4", "f6")

        // then
        assertThat(actual).isTrue()
        assertThat(board.board[3][3]).isNull()
        assertThat(board.board[4][4]).isNull()
        assertThat(board.board[5][5]).isSameAs(piece)
    }

    @Test
    fun `toString test`() {
        // when
        val actual = underTest.toString()

        // then
        assertThat(actual).isEqualTo(name)
    }
}
