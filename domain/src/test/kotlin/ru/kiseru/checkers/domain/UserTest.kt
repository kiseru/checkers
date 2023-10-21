package ru.kiseru.checkers.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.board.Cell
import ru.kiseru.checkers.domain.board.Piece
import ru.kiseru.checkers.domain.exception.CellException
import ru.kiseru.checkers.domain.exception.ConvertCellException
import ru.kiseru.checkers.domain.exception.PieceException
import ru.kiseru.checkers.domain.user.User
import ru.kiseru.checkers.domain.utils.Color

@ExtendWith(MockitoExtension::class)
class UserTest {

    private val name = "Some cool name"

    @InjectMocks
    private val underTest = User(name, Color.WHITE)

    @Mock
    private lateinit var board: Board

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn test when source cell isn't valid`(from: String) {
        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { underTest.makeTurn(from, "a2") }
    }

    @Test
    fun `makeTurn test when source cell hasn't piece`() {
        // given
        given(board.getCell(eq(2), eq(2))).willReturn(mock<Cell>())

        // when & then
        assertThatExceptionOfType(CellException::class.java)
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @Test
    fun `makeTurn when source cell hasn't player piece`() {
        // given
        val piece = mock<Piece>()
        given(piece.color).willReturn(Color.BLACK)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell)

        // when & then
        assertThatExceptionOfType(PieceException::class.java)
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn when destination cell name isn't valid`(to: String) {
        // given
        val piece = mock<Piece>()
        given(piece.color).willReturn(Color.WHITE)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell)

        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { underTest.makeTurn("b2", to) }
    }

    @Test
    fun `makeTurn test when destination cell has a piece`() {
        // given
        val piece = mock<Piece>()
        given(piece.color).willReturn(Color.WHITE)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        val destinationCell = mock<Cell>()
        given(destinationCell.piece).willReturn(mock<Piece>())

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell)
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell)

        // when & then
        assertThatExceptionOfType(CellException::class.java)
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @Test
    fun `makeTurn test when user can eat`() {
        // given
        val piece = mock<Piece>()
        given(piece.color).willReturn(Color.WHITE)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        val destinationCell = mock<Cell>()

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell)
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell)

        underTest.isCanEat = true

        // when & then
        assertThatNoException()
            .isThrownBy { underTest.makeTurn("b2", "c3") }
    }

    @Test
    fun `makeTurn test when user can't eat`() {
        // given
        val piece = mock<Piece>()
        given(piece.color).willReturn(Color.WHITE)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        val destinationCell = mock<Cell>()

        given(board.getCell(eq(2), eq(2))).willReturn(sourceCell)
        given(board.getCell(eq(3), eq(3))).willReturn(destinationCell)

        underTest.isCanEat = false

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