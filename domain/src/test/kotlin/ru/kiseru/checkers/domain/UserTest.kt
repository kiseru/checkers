package ru.kiseru.checkers.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.Assertions.assertThatNoException
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
    fun `makeTurn test when destination cell has a piece`() {
        // given
        board.board[1][1] = Man(Color.WHITE, 2, 2, board)
        board.board[2][2] = Man(Color.WHITE, 3, 3, board)

        underTest.color = Color.WHITE

        // when & then
        assertThatExceptionOfType(CellException::class.java)
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @Test
    fun `makeTurn test when user can eat`() {
        // given
        board.board[1][1] = Man(Color.WHITE, 2, 2, board)

        underTest.isCanEat = true
        underTest.color = Color.WHITE

        // when & then
        assertThatNoException()
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @Test
    fun `makeTurn test when user can't eat`() {
        // given
        board.board[1][1] = Man(Color.WHITE, 2, 2, board)

        underTest.isCanEat = false
        underTest.color = Color.WHITE

        // when
        underTest.makeTurn("b2", "c3")

        // then
        assertThat(underTest.isCanEat).isFalse()
    }

    @Test
    fun `toString test`() {
        // when
        val actual = underTest.toString()

        // then
        assertThat(actual).isEqualTo(name)
    }
}
