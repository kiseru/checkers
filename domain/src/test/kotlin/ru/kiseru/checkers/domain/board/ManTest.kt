package ru.kiseru.checkers.domain.board

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.willCallRealMethod
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.springframework.test.util.ReflectionTestUtils
import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.MustEatException
import ru.kiseru.checkers.domain.utils.Color

@ExtendWith(MockitoExtension::class)
class ManTest {

    private val sourceCellColumn = 4

    private val sourceCellRow = 4

    @InjectMocks
    private val underTest = Man(Color.WHITE)

    @Mock
    private lateinit var board: Board

    @Mock
    private lateinit var cell: Cell

    @Mock
    private lateinit var destinationCell: Cell

    @Test
    fun `isAbleToMoveTo when destination cell isn't empty`() {
        // given
        given(destinationCell.piece).willReturn(mock<Piece>())

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when enemy is nearby`() {
        // given
        val enemy = mock<Man>()
        ReflectionTestUtils.setField(enemy, "color", Color.BLACK)

        val cellWithEnemy = mock<Cell>()
        given(cellWithEnemy.piece).willReturn(enemy)

        given(board.getCell(anyInt(), anyInt())).willReturn(cellWithEnemy)

        given(cell.board).willReturn(board)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when cell diff more than one`() {
        // given
        val emptyCell = mock<Cell>()

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell)

        given(cell.board).willReturn(board)
        given(cell.diff(eq(destinationCell))).willReturn(2)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when there is no next row`() {
        // given
        val emptyCell = mock<Cell>()

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell)

        given(cell.board).willReturn(board)
        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(8)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when the player can move to destination cell`() {
        // given
        val emptyCell = mock<Cell>()

        given(board.getCell(eq(sourceCellRow + 1), eq(sourceCellColumn + 1))).willReturn(destinationCell)
        given(board.getCell(eq(sourceCellRow + 1), eq(sourceCellColumn - 1))).willReturn(destinationCell)
        given(board.getCell(eq(sourceCellRow - 1), eq(sourceCellColumn - 1))).willReturn(emptyCell)
        given(board.getCell(eq(sourceCellRow - 1), eq(sourceCellColumn + 1))).willReturn(emptyCell)

        given(cell.column).willReturn(sourceCellRow)
        given(cell.row).willReturn(sourceCellColumn)
        given(cell.board).willReturn(board)
        given(cell.diff(eq(destinationCell))).willReturn(1)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isAbleToEatTo test when diff is less than two`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(1)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when diff is more than two`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(3)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when destination cell isn't busy`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(2)

        given(destinationCell.piece).willReturn(mock<Piece>())

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when there are no sacrifice pieces`() {
        // given
        val cellWithSacrifice = mock<Cell>()

        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.between(eq(destinationCell))).willReturn(cellWithSacrifice)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when sacrifice piece has the same color`() {
        // given
        val sacrificePiece = mock<Man>()
        given(sacrificePiece.color).willReturn(Color.WHITE)

        val cellWithSacrifice = mock<Cell>()
        given(cellWithSacrifice.piece).willReturn(sacrificePiece)

        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.between(eq(destinationCell))).willReturn(cellWithSacrifice)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when sacrifice piece has enemy color`() {
        // given
        val sacrificePiece = mock<Man>()
        ReflectionTestUtils.setField(sacrificePiece, "color", Color.BLACK)

        val cellWithSacrifice = mock<Cell>()
        given(cellWithSacrifice.piece).willReturn(sacrificePiece)

        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.between(eq(destinationCell))).willReturn(cellWithSacrifice)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `analyzeAbilityOfMove test`() {
        // given
        val emptyCell = mock<Cell>()

        given(cell.board).willReturn(board)
        given(cell.column).willReturn(sourceCellColumn)
        given(cell.row).willReturn(sourceCellRow)
        given(cell.diff(eq(emptyCell))).willReturn(1)

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell)

        // when
        underTest.analyzeAbilityOfMove()

        // then
        assertThat(underTest.isCanMove).isTrue()
    }

    @CsvSource("8,WHITE", "1,BLACK")
    @ParameterizedTest
    fun `analyzeAbilityOfMove on the last line test`(row: Int, color: Color) {
        // given
        given(cell.row).willReturn(row)

        ReflectionTestUtils.setField(underTest, "color", color)

        // when
        underTest.analyzeAbilityOfMove()

        // then
        assertThat(underTest.isCanMove).isFalse()
    }

    @Test
    fun `analyzeAbilityOfEat test`() {
        // given
        val emptyCell = mock<Cell>()

        val enemyPiece = mock<Piece>()
        ReflectionTestUtils.setField(enemyPiece, "color", Color.BLACK)

        val cellWithEnemyPiece = mock<Cell>()
        given(cellWithEnemyPiece.piece).willReturn(enemyPiece)

        given(cell.board).willReturn(board)
        given(cell.column).willReturn(sourceCellColumn)
        given(cell.row).willReturn(sourceCellRow)
        given(cell.diff(eq(emptyCell))).willReturn(2)
        given(cell.between(eq(emptyCell))).willReturn(cellWithEnemyPiece)

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell)

        // when
        underTest.analyzeAbilityOfEat()

        // then
        assertThat(underTest.isCanEat).isTrue()
    }

    @Test
    fun `move test when man can eat`() {
        // given
        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(MustEatException::class.java)
            .isThrownBy { underTest.move(destinationCell) }
    }

    @Test
    fun `move test when man can't move`() {
        // given
        underTest.isCanEat = false
        underTest.isCanMove = false

        // then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(destinationCell) }
    }

    @Test
    fun `move test when man can't move to`() {
        // given
        underTest.isCanEat = false
        underTest.isCanMove = true

        given(destinationCell.piece).willReturn(mock<Piece>())

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(destinationCell) }
    }

    @ParameterizedTest
    @CsvSource("WHITE,4,4,5,5", "BLACK,5,5,4,4")
    fun `move test`(color: Color, sourceRow: Int, sourceColumn: Int, destinationRow: Int, destinationColumn: Int) {
        // given
        underTest.isCanEat = false
        underTest.isCanMove = true
        ReflectionTestUtils.setField(underTest, "color", color)

        given(board.getCell(anyInt(), anyInt())).willReturn(mock<Cell>())
        given(board.getCell(eq(destinationRow), eq(destinationColumn))).willReturn(destinationCell)

        willCallRealMethod().given(cell).piece = anyOrNull()
        given(cell.board).willReturn(board)
        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(sourceRow)
        given(cell.column).willReturn(sourceColumn)

        willCallRealMethod().given(destinationCell).piece = any<Piece>()

        // when
        underTest.move(destinationCell)

        // then
        val pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(pieceInDestinationCell).isSameAs(underTest)

        val pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece")
        assertThat(pieceInSourceCell).isNull()
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,7,1,8,2",
        "WHITE,7,3,8,2",
        "WHITE,7,3,8,4",
        "WHITE,7,5,8,4",
        "WHITE,7,5,8,4",
        "WHITE,7,5,8,6",
        "WHITE,7,7,8,6",
        "WHITE,7,7,8,8",
        "BLACK,2,2,1,1",
        "BLACK,2,2,1,3",
        "BLACK,2,4,1,3",
        "BLACK,2,4,1,5",
        "BLACK,2,6,1,5",
        "BLACK,2,6,1,7",
        "BLACK,2,8,1,7",
    )
    fun `move test when piece becomes king`(
        color: Color,
        sourceRow: Int,
        sourceColumn: Int,
        destinationRow: Int,
        destinationColumn: Int,
    ) {
        // given
        underTest.isCanEat = false
        underTest.isCanMove = true
        ReflectionTestUtils.setField(underTest, "color", color)

        given(board.getCell(anyInt(), anyInt())).willReturn(mock<Cell>())
        given(board.getCell(eq(destinationRow), eq(destinationColumn))).willReturn(destinationCell)

        given(cell.board).willReturn(board)
        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(sourceRow)
        given(cell.column).willReturn(sourceColumn)
        willCallRealMethod().given(cell).piece = anyOrNull()

        given(destinationCell.row).willReturn(destinationRow)
        willCallRealMethod().given(destinationCell).piece = any<Piece>()

        // when
        underTest.move(destinationCell)

        // then
        val pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(pieceInDestinationCell).isNotNull()
        assertThat(pieceInDestinationCell).isInstanceOf(King::class.java)
        assertThat((pieceInDestinationCell as King).color).isEqualTo(color)

        val pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece")
        assertThat(pieceInSourceCell).isNull()
    }

    @Test
    fun `eat test when piece can't eat`() {
        // given
        underTest.isCanEat = false

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(destinationCell) }
    }

    @Test
    fun `eat test when piece can't eat at some destination`() {
        // given
        underTest.isCanEat = true

        // then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(destinationCell) }
    }

    @Test
    fun `eat test`() {
        // given
        val enemy = mock<Piece>()
        ReflectionTestUtils.setField(enemy, "color", Color.BLACK)

        val targetCell = mock<Cell>()
        given(targetCell.piece).willReturn(enemy)
        willCallRealMethod().given(targetCell).piece = anyOrNull()

        given(cell.between(eq(destinationCell))).willReturn(targetCell)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        willCallRealMethod().given(cell).piece = anyOrNull()

        underTest.isCanEat = true

        willCallRealMethod().given(destinationCell).piece = any()

        // when
        underTest.eat(destinationCell)

        // then
        val pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece")
        assertThat(pieceInSourceCell).isNull()

        val pieceInTargetCell = ReflectionTestUtils.getField(targetCell, "piece")
        assertThat(pieceInTargetCell).isNull()

        val pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(pieceInDestinationCell).isSameAs(underTest)
    }

    @Test
    fun `eat test when white man should become king`() {
        // given
        given(destinationCell.row).willReturn(8)
        willCallRealMethod().given(destinationCell).piece = any<Piece>()

        val enemy = mock<Piece>()
        ReflectionTestUtils.setField(enemy, "color", Color.BLACK)

        val targetCell = mock<Cell>()
        given(targetCell.piece).willReturn(enemy)
        willCallRealMethod().given(targetCell).piece = anyOrNull()

        given(cell.between(eq(destinationCell))).willReturn(targetCell)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        willCallRealMethod().given(cell).piece = anyOrNull()

        underTest.isCanEat = true

        // when
        underTest.eat(destinationCell)

        // then
        val pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece")
        assertThat(pieceInSourceCell).isNull()

        val pieceInTargetCell = ReflectionTestUtils.getField(targetCell, "piece")
        assertThat(pieceInTargetCell).isNull()

        val pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(pieceInDestinationCell).isNotNull()
        assertThat(pieceInDestinationCell).isInstanceOf(King::class.java)
        assertThat((pieceInDestinationCell as King).color).isEqualTo(Color.WHITE)
    }

    @Test
    fun `eat test when black man should become king`() {
        // given
        given(destinationCell.row).willReturn(1)
        willCallRealMethod().given(destinationCell).piece = any<Piece>()

        val enemy = mock<Piece>()
        ReflectionTestUtils.setField(enemy, "color", Color.WHITE)

        val targetCell = mock<Cell>()
        given(targetCell.piece).willReturn(enemy)
        willCallRealMethod().given(targetCell).piece = anyOrNull()

        given(cell.between(eq(destinationCell))).willReturn(targetCell)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        willCallRealMethod().given(cell).piece = anyOrNull()

        ReflectionTestUtils.setField(underTest, "color", Color.BLACK)
        underTest.isCanEat = true

        // when
        underTest.eat(destinationCell)

        // then
        val pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece")
        assertThat(pieceInSourceCell).isNull()

        val pieceInTargetCell = ReflectionTestUtils.getField(targetCell, "piece")
        assertThat(pieceInTargetCell).isNull()

        val pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(pieceInDestinationCell).isNotNull()
        assertThat(pieceInDestinationCell).isInstanceOf(King::class.java)
        assertThat((pieceInDestinationCell as King).color).isEqualTo(Color.BLACK)
    }

    @Test
    fun `creating test`() {
        // when
        val man = Man(Color.BLACK)

        // then
        assertThat(man.color).isEqualTo(Color.BLACK)
    }

    @Test
    fun `isMan test`() {
        // when
        val actual = underTest.isMan()

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isKing test`() {
        // when
        val actual = underTest.isKing()

        // then
        assertThat(actual).isFalse()
    }

    @ParameterizedTest
    @CsvSource("WHITE,piece white-man", "BLACK,piece black-man")
    fun `getCssClass test`(color: Color, expected: String) {
        // given
        ReflectionTestUtils.setField(underTest, "color", color)

        // when
        val actual = underTest.getCssClass()

        // then
        assertThat(actual).isEqualTo(expected)
    }
}
