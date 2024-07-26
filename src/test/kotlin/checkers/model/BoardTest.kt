package checkers.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.exception.CellException
import ru.kiseru.checkers.exception.CellIsBusyException
import ru.kiseru.checkers.exception.ConvertCellException
import ru.kiseru.checkers.exception.PieceException
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.Man
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class BoardTest {

    private val boardSize = 8

    private lateinit var underTest: Board

    @BeforeEach
    fun setUp() {
        underTest = Board(UUID.randomUUID())
    }

    @Test
    fun `init test`() {
        // then
        val board = underTest.board
        assertThat(board).isNotNull()
        assertThat(board.size).isEqualTo(boardSize)

        board.forEach { assertThat(it.size).isEqualTo(boardSize) }

        val pieceCount = board.flatMap { it.asSequence() }
            .count { it != null }
        assertThat(pieceCount).isEqualTo(24)

        val whitePieceCount = board.flatMap { it.asSequence() }
            .count { it?.color == Color.WHITE }
        assertThat(whitePieceCount).isEqualTo(12)

        val blackPieceCount = board.flatMap { it.asSequence() }
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
    fun `init white pieces test`(row: Int, column: Int, expected: Color) {
        // given
        val board = underTest.board
        val piece = board[row - 1][column - 1]

        // then
        assertThat(piece).isNotNull
        assertThat(piece?.color).isEqualTo(expected)
    }

    @Test
    fun `makeTurn test when user can eat`() {
        // given
        val board = underTest.board
        board[3][3] = board[5][5]
        board[5][5] = null

        val sourcePiece = board[2][2]

        // when
        underTest.makeTurn(Color.WHITE, "c3", "e5")

        // then
        assertThat(board[2][2]).isNull()
        assertThat(board[3][3]).isNull()
        assertThat(board[4][4]).isSameAs(sourcePiece)
    }

    @Test
    fun `makeTurn test when source cell is empty`() {
        // when & then
        assertThatExceptionOfType(CellException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, "d4", "e5") }
    }

    @Test
    fun `makeTurn test when destination cell is busy`() {
        // when & then
        assertThatExceptionOfType(CellIsBusyException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, "b2", "c3") }
    }

    @Test
    fun `makeTurn test when user cannot move`() {
        // when
        underTest.makeTurn(Color.WHITE, "c3", "d4")

        // then
        val board = underTest.board
        assertThat(board[2][2]).isNull()
        assertThat(board[3][3]).isNotNull
    }

    @Test
    fun `analyze test when user cannot eat`() {
        // when
        val actual = underTest.analyze(Color.WHITE)

        // then
        assertThat(actual).isEqualTo(false)
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn test when source cell isn't valid`(from: String) {
        // given
        clearBoard(underTest)

        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, from, "a2") }
    }

    @Test
    fun `makeTurn test when source cell hasn't piece`() {
        // given
        clearBoard(underTest)

        // when & then
        assertThatExceptionOfType(CellException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, "b2", "c3") }
    }

    @Test
    fun `makeTurn test when source cell hasn't player piece`() {
        // given
        clearBoard(underTest)

        underTest.board[1][1] = Man(Color.BLACK)

        // when & then
        assertThatExceptionOfType(PieceException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, "b2", "c3") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn test when destination cell name isn't valid`(to: String) {
        // given
        clearBoard(underTest)

        underTest.board[1][1] = Man(Color.WHITE)

        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, "b2", to) }
    }

    @Test
    fun `makeTurn test when can move`() {
        // given
        clearBoard(underTest)

        val piece = Man(Color.WHITE)
        underTest.board[3][3] = piece

        // when
        val actual = underTest.makeTurn(Color.WHITE, "d4", "e5")

        // then
        assertThat(actual).isFalse()
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isSameAs(piece)
    }

    @Test
    fun `makeTurn test when can eat`() {
        // given
        clearBoard(underTest)

        val piece = Man(Color.WHITE)
        underTest.board[3][3] = piece

        underTest.board[4][4] = Man(Color.BLACK)

        // when
        val actual = underTest.makeTurn(Color.WHITE, "d4", "f6")

        // then
        assertThat(actual).isFalse()
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isNull()
        assertThat(underTest.board[5][5]).isSameAs(piece)
    }

    @Test
    fun `makeTurn test when can eat two pieces`() {
        // given
        clearBoard(underTest)

        val piece = Man(Color.WHITE)
        underTest.board[3][3] = piece

        underTest.board[4][4] = Man(Color.BLACK)
        underTest.board[6][6] = Man(Color.BLACK)

        // when
        val actual = underTest.makeTurn(Color.WHITE, "d4", "f6")

        // then
        assertThat(actual).isTrue()
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isNull()
        assertThat(underTest.board[5][5]).isSameAs(piece)
    }

    private fun clearBoard(board: Board) {
        for (cells in board.board) {
            for (i in cells.indices) {
                cells[i] = null
            }
        }
    }
}
