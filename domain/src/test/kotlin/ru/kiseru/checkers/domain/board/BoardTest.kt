package ru.kiseru.checkers.domain.board

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.willCallRealMethod
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.springframework.test.util.ReflectionTestUtils
import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.CellIsBusyException
import ru.kiseru.checkers.domain.exception.CellIsEmptyException
import ru.kiseru.checkers.domain.exception.CellNotFoundException
import ru.kiseru.checkers.domain.user.User
import ru.kiseru.checkers.domain.utils.Color

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

        board.flatMap { it.asSequence() }
            .forEach { assertThat(it).isNotNull }
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
        val cell = board[row - 1][column - 1]
        val piece = cell.piece

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
        // given
        val sourceCell = mock<Cell>()
        val destinationCell = mock<Cell>()

        // when & then
        assertThatExceptionOfType(CellIsEmptyException::class.java)
            .isThrownBy { underTest.eat(sourceCell, destinationCell) }
    }

    @Test
    fun testEatWhileDestinationCellIsBusy() {
        // given
        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(mock<Piece>())

        val destinationCell = mock<Cell>()
        given(destinationCell.piece).willReturn(mock<Piece>())

        // when & then
        assertThatExceptionOfType(CellIsBusyException::class.java)
            .isThrownBy { underTest.eat(sourceCell, destinationCell) }
    }

    @Test
    fun testEatWhileCannotEat() {
        // given
        val destinationCell = mock<Cell>()

        val piece = mock<Piece>()
        given(piece.isAbleToEatTo(eq(destinationCell))).willReturn(false)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(sourceCell, destinationCell) }
    }

    @Test
    fun testEatWhileCanEat() {
        // given
        val destinationCell = mock<Cell>()

        val piece = mock<Piece>()
        given(piece.isAbleToEatTo(eq(destinationCell))).willReturn(true)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        // when
        underTest.eat(sourceCell, destinationCell)

        // then
        then(piece).should(times(1)).eat(eq(destinationCell))
    }

    @Test
    fun testMoveWhileSourceCellIsEmpty() {
        // given
        val destinationCell = mock<Cell>()

        val sourceCell = mock<Cell>()

        // when & then
        assertThatExceptionOfType(CellIsEmptyException::class.java)
            .isThrownBy { underTest.move(sourceCell, destinationCell) }
    }

    @Test
    fun testMoveWhileDestinationCellIsBusy() {
        // given
        val destinationCell = mock<Cell>()
        given(destinationCell.piece).willReturn(mock<Piece>())

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(mock<Piece>())

        // when & then
        assertThatExceptionOfType(CellIsBusyException::class.java)
            .isThrownBy { underTest.move(sourceCell, destinationCell) }
    }

    @Test
    fun testMoveWhileCannotMove() {
        // given
        val destinationCell = mock<Cell>()

        val piece = mock<Piece>()
        given(piece.isAbleToMoveTo(eq(destinationCell))).willReturn(false)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(sourceCell, destinationCell) }
    }

    @Test
    fun testMoveWhileCanMove() {
        // given
        val destinationCell = mock<Cell>()

        val piece = mock<Piece>()
        given(piece.isAbleToMoveTo(eq(destinationCell))).willReturn(true)

        val sourceCell = mock<Cell>()
        given(sourceCell.piece).willReturn(piece)

        // when
        underTest.move(sourceCell, destinationCell)

        // then
        then(piece).should(times(1)).move(eq(destinationCell))
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

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun testAnalyzeWhileCannotEat(isCanEat: Boolean) {
        // given
        val user = mock<User>()
        given(user.color).willReturn(Color.WHITE)

        willCallRealMethod().given(user).isCanEat = anyBoolean()

        val whiteCell = mock<Cell>()

        val emptyCell = mock<Cell>()
        given(emptyCell.piece).willReturn(null)

        val playerPiece = mock<Piece>()
        given(playerPiece.color).willReturn(Color.WHITE)
        given(playerPiece.isCanEat).willReturn(isCanEat)

        val cellWithPlayerPiece = mock<Cell>()
        given(cellWithPlayerPiece.piece).willReturn(playerPiece)

        val board = mock<Board>()
        val matrix = Array(8) {
            Array(8) { emptyCell }
        }
        matrix[0][1] = whiteCell
        matrix[0][2] = cellWithPlayerPiece
        ReflectionTestUtils.setField(board, "board", matrix)
        willCallRealMethod().given(board).analyze(eq(user))

        // when
        board.analyze(user)

        // then
        val actual = ReflectionTestUtils.getField(user, "isCanEat")
        assertThat(actual).isEqualTo(isCanEat)
    }

    @ParameterizedTest
    @CsvSource(
        "1, 5",
        "5, 1",
        "8, 5",
        "5, 8",
    )
    fun testGetCellWhileCellExists(row: Int, column: Int) {
        // given
        val board = Board()

        // when
        val actual = board.getCell(row, column)

        // then
        assertThat(actual).isNotNull()
    }

    @ParameterizedTest
    @CsvSource(
        "0, 1",
        "1, 0",
        "9, 1",
        "1, 9",
    )
    fun testGetCellWhileCellDoesNotExist(row: Int, column: Int) {
        // given
        val board = Board()

        // when & then
        assertThatExceptionOfType(CellNotFoundException::class.java)
            .isThrownBy { board.getCell(row, column) }
    }
}