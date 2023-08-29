package ru.kiseru.checkers.board

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.anyInt
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willCallRealMethod
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.springframework.test.util.ReflectionTestUtils
import ru.kiseru.checkers.exception.CannotEatException
import ru.kiseru.checkers.exception.CannotMoveException
import ru.kiseru.checkers.exception.MustEatException
import ru.kiseru.checkers.utils.Color

@ExtendWith(MockitoExtension::class)
class KingTest {

    @Mock
    lateinit var cell: Cell

    @Mock
    lateinit var destinationCell: Cell

    @InjectMocks
    var underTest: King = King(Color.WHITE)

    @Test
    fun `isAbleToMoveTo test when diff is minus one`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(-1)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when destinationCell isn't empty`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(1)

        given(destinationCell.piece).willReturn(mock<Piece>())

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when source cell and destination cell aren't on the same diagonal`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)

        given(destinationCell.row).willReturn(3)
        given(destinationCell.column).willReturn(5)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when there is a piece between source cell and destination cell`() {
        // given
        val notEmptyCell = mock<Cell>()
        given(notEmptyCell.piece).willReturn(mock<Piece>())

        val board = mock<Board>()
        given(board.getCell(anyInt(), anyInt())).willReturn(mock<Cell>())
        given(board.getCell(eq(4), eq(4))).willReturn(notEmptyCell)

        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.board).willReturn(board)

        given(destinationCell.row).willReturn(6)
        given(destinationCell.column).willReturn(6)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when there are no pieces between source cell and destination cell`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)

        given(destinationCell.row).willReturn(3)
        given(destinationCell.column).willReturn(3)

        // when
        val actual = underTest.isAbleToMoveTo(destinationCell)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isAbleToEatTo test when destination cell isn't empty`() {
        // given
        given(destinationCell.piece).willReturn(mock<Piece>())

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when source cell and destination cell aren't on the same diagonal`() {
        // given
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)

        given(destinationCell.row).willReturn(3)
        given(destinationCell.column).willReturn(5)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when diff is less than two`() {
        // given
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(1)

        given(destinationCell.row).willReturn(3)
        given(destinationCell.column).willReturn(3)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when there are no pieces between source cell and destination cell`() {
        // given
        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(mock<Cell>())

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when there's a player checker between source cell and destination cell`() {
        // given
        val cellWithPlayerPiece = createCellWithPiece(Color.WHITE)

        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithPlayerPiece)

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when there are two enemy pieces between source cell and destination cell`() {
        // given
        val cellWithEnemyPiece = createCellWithPiece(Color.BLACK)

        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece)
        given(board.getCell(eq(4), eq(4))).willReturn(cellWithEnemyPiece)

        given(destinationCell.row).willReturn(5)
        given(destinationCell.column).willReturn(5)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(3)
        given(cell.board).willReturn(board)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when there's an enemy piece between source cell and destination cell`() {
        // given
        val cellWithEnemyPiece = createCellWithPiece(Color.BLACK)

        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece)

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)

        // when
        val actual = underTest.isAbleToEatTo(destinationCell)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isKing test`() {
        // when
        val actual = underTest.isKing()

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isMan test`() {
        // when
        val actual = underTest.isMan()

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `move test when can eat`() {
        // given
        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(MustEatException::class.java)
            .isThrownBy { underTest.move(destinationCell) }
    }

    @Test
    fun `move test when can't move`() {
        // given
        underTest.isCanEat = false
        underTest.isCanMove = false

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(destinationCell) }
    }

    @Test
    fun `move test when can't move to destination cell`() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(-1)

        underTest.isCanEat = false
        underTest.isCanMove = true

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(destinationCell) }
    }

    @Test
    fun `move test when can move to destination cell`() {
        // given
        ReflectionTestUtils.setField(cell, "piece", underTest)
        given(cell.diff(eq(destinationCell))).willReturn(1)
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        willCallRealMethod().given(cell).piece = any()

        ReflectionTestUtils.setField(destinationCell, "piece", null)
        given(destinationCell.row).willReturn(3)
        given(destinationCell.column).willReturn(3)
        willCallRealMethod().given(destinationCell).piece = any()

        underTest.isCanEat = false
        underTest.isCanMove = true

        // when
        underTest.move(destinationCell)

        // then
        val cellPiece = ReflectionTestUtils.getField(cell, "piece")
        assertThat(cellPiece).isNull()

        val destinationCellPiece = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(destinationCellPiece).isSameAs(underTest)

        assertThat(underTest.cell).isSameAs(destinationCell)
    }

    @Test
    fun `eat test when can't eat`() {
        // given
        underTest.isCanEat = false

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(destinationCell) }
    }

    @Test
    fun `eat test when can't eat to destination cell`() {
        // given
        given(destinationCell.piece).willReturn(mock<Piece>())

        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(destinationCell) }
    }

    @Test
    fun `eat test when there is no sacrificed piece source cell and destination cell`() {
        // given
        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(mock<Cell>())

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)

        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(destinationCell) }
    }

    @Test
    fun `eat test when there is a player piece between source cell and destination cell`() {
        // given
        val cellWithPlayerPiece = createCellWithPiece(Color.WHITE)

        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithPlayerPiece)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)

        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(destinationCell) }
    }

    @Test
    fun `eat test when there is an enemy piece between source cell and destination cell`() {
        // given
        val piece = mock<Piece>()
        given(piece.color).willReturn(Color.BLACK)

        val cellWithEnemyPiece = mock<Cell>()
        given(cellWithEnemyPiece.piece).willReturn(piece)
        willCallRealMethod().given(cellWithEnemyPiece).piece = any()

        given(piece.cell).willReturn(cellWithEnemyPiece)

        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)
        willCallRealMethod().given(cell).piece = any()

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)
        willCallRealMethod().given(destinationCell).piece = any()

        underTest.isCanEat = true

        // when
        underTest.eat(destinationCell)

        // then
        val cellPiece = ReflectionTestUtils.getField(cell, "piece")
        assertThat(cellPiece).isNull()

        val destinationCellPiece = ReflectionTestUtils.getField(destinationCell, "piece")
        assertThat(destinationCellPiece).isSameAs(underTest)

        val sacrificedPiece = ReflectionTestUtils.getField(cellWithEnemyPiece, "piece")
        assertThat(sacrificedPiece).isNull()

        assertThat(underTest.cell).isSameAs(destinationCell)
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE, #",
        "BLACK, &",
    )
    fun `toString test`(color: Color, expected: String) {
        // given
        ReflectionTestUtils.setField(underTest, "color", color)

        // when
        val actual = underTest.toString()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `analyzeAbilityOfMove test when there are no moves`() {
        // given
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(destinationCell)).willReturn(-1)
        given(cell.getNear(anyInt(), anyInt())).willReturn(destinationCell)

        // when
        underTest.analyzeAbilityOfMove()

        // then
        assertThat(underTest.isCanMove).isFalse()
    }

    @Test
    fun `analyzeAbilityOfMove test when there's an available move`() {
        // given
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.diff(destinationCell)).willReturn(1)
        given(cell.getNear(eq(1), eq(1))).willReturn(destinationCell)

        given(destinationCell.row).willReturn(3)
        given(destinationCell.column).willReturn(3)

        // when
        underTest.analyzeAbilityOfMove()

        // then
        assertThat(underTest.isCanMove).isTrue()
    }

    @Test
    fun `analyzeAbilityOfEat test when there are no moves`() {
        // given
        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.getNear(anyInt(), anyInt())).willReturn(destinationCell)
        given(cell.diff(eq(destinationCell))).willReturn(1)

        // when
        underTest.analyzeAbilityOfEat()

        // then
        assertThat(underTest.isCanEat).isFalse()
    }

    @Test
    fun `analyzeAbilityOfEat test when there's an available move`() {
        // given
        val cellWithEnemyPiece = createCellWithPiece(Color.BLACK)

        val board = mock<Board>()
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece)

        given(cell.row).willReturn(2)
        given(cell.column).willReturn(2)
        given(cell.getNear(eq(2), eq(2))).willReturn(destinationCell)
        given(cell.diff(eq(destinationCell))).willReturn(2)
        given(cell.board).willReturn(board)

        given(destinationCell.row).willReturn(4)
        given(destinationCell.column).willReturn(4)

        // when
        underTest.analyzeAbilityOfEat()

        // then
        assertThat(underTest.isCanEat).isTrue()
    }

    private fun createCellWithPiece(pieceColor: Color): Cell {
        val piece = mock<Piece>()
        given(piece.color).willReturn(pieceColor)

        val cell = mock<Cell>()
        given(cell.piece).willReturn(piece)

        return cell
    }
}
