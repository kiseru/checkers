package ru.kiseru.checkers.domain.board

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CellIsBusyException
import ru.kiseru.checkers.domain.exception.CellIsEmptyException
import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.utils.Color
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@ExtendWith(MockitoExtension::class)
class BoardTest {

    private val boardSize = 8

    private lateinit var underTest: Board

    @BeforeEach
    fun setUp() {
        underTest = Board()
    }

    @Test
    fun testInitializing() {
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
    fun testWhitePieceInitialization(row: Int, column: Int, expected: Color) {
        // given
        val board = underTest.board
        val piece = board[row - 1][column - 1]

        // then
        assertThat(piece).isNotNull
        assertThat(piece?.color).isEqualTo(expected)
    }

    @Test
    fun testIsGamingWhileThereIsPiecesOnBoard() {
        // when
        val actual = underTest.isGaming()

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun testIsGamingWhileThereIsOnlyWhitePiecesOnBoard() {
        // given
        ReflectionTestUtils.setField(underTest, "blackPieces", 0)

        // when
        val actual = underTest.isGaming()

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun testIsGamingWhileThereIsOnlyBlackPiecesObBoard() {
        // given
        ReflectionTestUtils.setField(underTest, "whitePieces", 0)

        // when
        val actual = underTest.isGaming()

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun testEatWhileSourceCellIsEmpty() {
        // when & then
        assertThatExceptionOfType(CellIsEmptyException::class.java)
            .isThrownBy { underTest.eat(5 to 5, 6 to 6) }
    }

    @Test
    fun testEatWhileDestinationCellIsBusy() {
        // given
        val board = underTest.board
        board[4][4] = board[6][6]
        board[6][6] = null
        board[3][3] = board[2][2]
        board[2][2] = null

        // when & then
        assertThatExceptionOfType(CellIsBusyException::class.java)
            .isThrownBy { underTest.eat(4 to 4, 6 to 6) }
    }

    @Test
    fun testEatWhileCannotEat() {
        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(3 to 3, 5 to 5) }
    }

    @Test
    fun testEatWhileCanEat() {
        // given
        val board = underTest.board
        board[3][3] = board[5][5]
        board[5][5] = null

        val sourcePiece = board[2][2]

        underTest.analyze(Color.WHITE)

        // when
        underTest.eat(3 to 3, 5 to 5)

        // then
        assertThat(board[2][2]).isNull()
        assertThat(board[3][3]).isNull()
        assertThat(board[4][4]).isSameAs(sourcePiece)
    }

    @Test
    fun testMoveWhileSourceCellIsEmpty() {
        // when & then
        assertThatExceptionOfType(CellIsEmptyException::class.java)
            .isThrownBy { underTest.move(4 to 4, 5 to 5) }
    }

    @Test
    fun testMoveWhileDestinationCellIsBusy() {
        // when & then
        assertThatExceptionOfType(CellIsBusyException::class.java)
            .isThrownBy { underTest.move(2 to 2, 3 to 3) }
    }

    @Test
    fun testMoveWhileCanMove() {
        // when
        underTest.move(3 to 3, 4 to 4)

        // then
        val board = underTest.board
        assertThat(board[2][2]).isNull()
        assertThat(board[3][3]).isNotNull
    }

    @ParameterizedTest
    @CsvSource(
        "12, 11",
        "0, 0",
    )
    fun testDecrementWhitePieceCount(initialCount: Int, expectedCount: Int) {
        // given
        ReflectionTestUtils.setField(underTest, "whitePieces", initialCount)

        // when
        underTest.decrementWhitePieceCount()

        // then
        val actual = ReflectionTestUtils.getField(underTest, "whitePieces")
        assertThat(actual).isEqualTo(expectedCount)
    }

    @ParameterizedTest
    @CsvSource(
        "12, 11",
        "0, 0",
    )
    fun testDecrementBlackPieceCount(initialCount: Int, expectedCount: Int) {
        // given
        ReflectionTestUtils.setField(underTest, "blackPieces", initialCount)

        // when
        underTest.decrementBlackPieceCount()

        // then
        val actual = ReflectionTestUtils.getField(underTest, "blackPieces")
        assertThat(actual).isEqualTo(expectedCount)
    }

    @Test
    fun testAnalyzeWhileCannotEat() {
        // when
        val actual = underTest.analyze(Color.WHITE)

        // then
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `test waitNewVersion`() {
        // given
        thread {
            TimeUnit.SECONDS.sleep(1)
            underTest.move(3 to 3, 4 to 4)
        }

        // when
        assertThatNoException()
            .isThrownBy { underTest.waitNewVersion(1) }

        // then
        assertThat(underTest.version).isEqualTo(2)
    }

    @ParameterizedTest
    @CsvSource("0,4", "4, 0", "9,4", "4,9")
    fun `test move while source does not exist`(row: Int, column: Int) {
        // when & then
        assertThatExceptionOfType(CellNotFoundException::class.java)
            .isThrownBy { underTest.move(row to column, 1 to 1) }
    }
}
