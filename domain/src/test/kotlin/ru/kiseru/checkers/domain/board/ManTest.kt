package ru.kiseru.checkers.domain.board

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.domain.exception.CannotEatException
import ru.kiseru.checkers.domain.exception.CannotMoveException
import ru.kiseru.checkers.domain.exception.MustEatException
import ru.kiseru.checkers.domain.utils.Color

@ExtendWith(MockitoExtension::class)
class ManTest {

    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        board = Board()
        for (cells in board.board) {
            for (i in cells.indices) {
                cells[i] = null
            }
        }
    }

    @Test
    fun `isAbleToMoveTo when destination cell isn't empty`() {
        // given
        val underTest = Man(Color.WHITE, 2, 2, board)
        board.board[1][1] = underTest

        board.board[2][2] = Man(Color.WHITE, 3, 3, board)

        // when
        val actual = underTest.isAbleToMoveTo(3 to 3)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when enemy is nearby`() {
        // given
        val underTest = Man(Color.WHITE, 2, 2, board)
        board.board[2][2] = underTest

        board.board[3][3] = Man(Color.BLACK, 4, 4, board)

        // when
        val actual = underTest.isAbleToMoveTo(4 to 2)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when cell diff more than one`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        // when
        val actual = underTest.isAbleToMoveTo(5 to 5)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when there is no next row`() {
        // given
        val underTest = Man(Color.WHITE, 8, 4, board)
        board.board[7][3] = underTest

        // when
        val actual = underTest.isAbleToMoveTo(8 to 2)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToMoveTo test when the player can move to destination cell`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        // when
        val actual = underTest.isAbleToMoveTo(4 to 4)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isAbleToMoveTo test when the player can move to destination cell and there's an enemy piece nearby`() {
        // given
        val underTest = Man(Color.WHITE, 7, 7, board)
        board.board[6][6] = underTest

        board.board[7][7] = Man(Color.BLACK, 8, 8, board)

        // when
        val actual = underTest.isAbleToMoveTo(8 to 6)

        // then
        assertThat(actual).isTrue()
    }

    @ParameterizedTest
    @CsvSource("4,4", "6,6")
    fun `isAbleToEatTo test when diff is less than two`(destinationRow: Int, destinationColumn: Int) {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        // when
        val actual = underTest.isAbleToEatTo(destinationRow to destinationColumn)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when destination cell is busy`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        board.board[4][4] = Man(Color.WHITE, 5, 5, board)

        // when
        val actual = underTest.isAbleToEatTo(5 to 5)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when there are no sacrifice pieces`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        // when
        val actual = underTest.isAbleToEatTo(5 to 5)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when sacrifice piece has the same color`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        board.board[3][3] = Man(Color.WHITE, 4, 4, board)

        // when
        val actual = underTest.isAbleToEatTo(5 to 5)

        // then
        assertThat(actual).isFalse()
    }

    @Test
    fun `isAbleToEatTo test when sacrifice piece has enemy color`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        board.board[3][3] = Man(Color.BLACK, 4, 4, board)

        // when
        val actual = underTest.isAbleToEatTo(5 to 5)

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `analyzeAbilityOfMove test`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest

        // when
        underTest.analyzeAbilityOfMove()

        // then
        assertThat(underTest.isCanMove).isTrue()
    }

    @CsvSource(
        "8,2,WHITE",
        "8,4,WHITE",
        "8,6,WHITE",
        "8,8,WHITE",
        "1,1,BLACK",
        "1,3,BLACK",
        "1,5,BLACK",
        "1,7,BLACK",
    )
    @ParameterizedTest
    fun `analyzeAbilityOfMove on the last line test`(row: Int, column: Int, color: Color) {
        // given
        val underTest = Man(color, row, column, board)
        board.board[row - 1][column - 1] = underTest

        // when
        underTest.analyzeAbilityOfMove()

        // then
        assertThat(underTest.isCanMove).isFalse()
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,4,4,BLACK,5,3,true",
        "WHITE,4,4,BLACK,5,5,true",
        "WHITE,4,4,WHITE,5,3,false",
        "WHITE,4,4,WHITE,5,5,false",
        "WHITE,4,4,WHITE,2,2,false",
        "BLACK,4,4,WHITE,3,3,true",
        "BLACK,4,4,WHITE,3,5,true",
        "BLACK,4,4,BLACK,5,5,false",
        "BLACK,4,4,BLACK,3,5,false",
        "BLACK,4,4,BLACK,2,2,false",
    )
    fun `analyzeAbilityOfEat test`(
        sourceColor: Color,
        sourceRow: Int,
        sourceColumn: Int,
        targetColor: Color,
        targetRow: Int,
        targetColumn: Int,
        expected: Boolean,
    ) {
        // given
        val underTest = Man(sourceColor, sourceRow, sourceColumn, board)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest

        board.board[targetRow - 1][targetColumn - 1] = Man(targetColor, targetRow, targetColumn, board)

        // when
        underTest.analyzeAbilityOfEat()

        // then
        assertThat(underTest.isCanEat).isEqualTo(expected)
    }

    @Test
    fun `move test when man can eat`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest
        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(MustEatException::class.java)
            .isThrownBy { underTest.move(4 to 4) }
    }

    @Test
    fun `move test when man can't move`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest
        underTest.isCanEat = false
        underTest.isCanMove = false

        // then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(4 to 4) }
    }

    @Test
    fun `move test when man can't move to`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest
        underTest.isCanEat = false
        underTest.isCanMove = true

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java)
            .isThrownBy { underTest.move(5 to 3) }
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,4,2,5,1,5,3",
        "WHITE,4,2,3,1,5,3",
        "WHITE,3,7,4,8,4,6",
        "WHITE,3,7,2,8,4,6",
        "BLACK,4,2,5,1,3,3",
        "BLACK,4,2,3,1,3,3",
        "BLACK,3,7,4,8,2,6",
        "BLACK,3,7,2,8,2,6",
    )
    fun `move test when enemy piece at the edge of the board`(
        color: Color,
        sourceRow: Int,
        sourceColumn: Int,
        enemyRow: Int,
        enemyColumn: Int,
        destinationRow: Int,
        destinationColumn: Int,
    ) {
        // given
        val underTest = Man(color, sourceRow, sourceColumn, board)
        underTest.isCanMove = true
        board.board[sourceRow - 1][sourceColumn - 1] = underTest

        val enemyColor = when(color) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        val enemyPiece = Man(enemyColor, enemyRow, enemyColumn, board)
        board.board[enemyRow - 1][enemyColumn - 1] = enemyPiece

        // when
        underTest.move(destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isSameAs(enemyPiece)
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(underTest)
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,4,4,5,5",
        "WHITE,4,4,5,3",
        "BLACK,5,5,4,4",
        "BLACK,5,5,4,6",
    )
    fun `move test`(color: Color, sourceRow: Int, sourceColumn: Int, destinationRow: Int, destinationColumn: Int) {
        // given
        val underTest = Man(color, sourceRow, sourceColumn, board)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest
        underTest.isCanEat = false
        underTest.isCanMove = true

        // when
        underTest.move(destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(underTest)
        assertThat(underTest.row).isEqualTo(destinationRow)
        assertThat(underTest.column).isEqualTo(destinationColumn)
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
        val underTest = Man(color, sourceRow, sourceColumn, board)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest
        underTest.isCanEat = false
        underTest.isCanMove = true

        // when
        underTest.move(destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        val piece = board.board[destinationRow - 1][destinationColumn - 1]
        assertThat(piece).isInstanceOf(King::class.java)
        assertThat(piece?.color).isEqualTo(color)
        assertThat(piece?.row).isEqualTo(destinationRow)
        assertThat(piece?.column).isEqualTo(destinationColumn)
    }

    @Test
    fun `eat test when piece can't eat`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest
        underTest.isCanEat = false

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(4 to 4) }
    }

    @Test
    fun `eat test when piece can't eat at some destination`() {
        // given
        val underTest = Man(Color.WHITE, 3, 3, board)
        board.board[2][2] = underTest
        underTest.isCanEat = true

        // then
        assertThatExceptionOfType(CannotEatException::class.java)
            .isThrownBy { underTest.eat(4 to 4) }
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,4,4,5,5,6,6",
        "WHITE,4,4,3,3,2,2",
        "WHITE,4,4,5,3,6,2",
        "WHITE,4,4,3,5,2,6",
        "BLACK,4,4,5,5,6,6",
        "BLACK,4,4,3,3,2,2",
        "BLACK,4,4,5,3,6,2",
        "BLACK,4,4,3,5,2,6",
    )
    fun `eat test`(
        sourceColor: Color,
        sourceRow: Int,
        sourceColumn: Int,
        enemyRow: Int,
        enemyColumn: Int,
        destinationRow: Int,
        destinationColumn: Int,
    ) {
        // given
        val underTest = Man(sourceColor, sourceRow, sourceColumn, board)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest
        underTest.isCanEat = true

        val enemyColor = when(sourceColor) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        board.board[enemyRow - 1][enemyColumn - 1] = Man(enemyColor, enemyRow, enemyColumn, board)

        // when
        underTest.eat(destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isNull()
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(underTest)
        assertThat(underTest.row).isEqualTo(destinationRow)
        assertThat(underTest.column).isEqualTo(destinationColumn)
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,6,6,7,7,8,8",
        "BLACK,3,3,2,2,1,1",
    )
    fun `eat test when man should become king`(
        color: Color,
        sourceRow: Int,
        sourceColumn: Int,
        enemyRow: Int,
        enemyColumn: Int,
        destinationRow: Int,
        destinationColumn: Int,
    ) {
        // given
        val underTest = Man(color, sourceRow, sourceColumn, board)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest
        underTest.isCanEat = true

        val enemyColor = when (color) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        board.board[enemyRow - 1][enemyColumn - 1] = Man(enemyColor, enemyRow, enemyColumn, board)

        // when
        underTest.eat(destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isNull()
        val piece = board.board[destinationRow - 1][destinationColumn - 1]
        assertThat(piece).isInstanceOf(King::class.java)
        assertThat(piece?.color).isEqualTo(color)
        assertThat(piece?.row).isEqualTo(destinationRow)
        assertThat(piece?.column).isEqualTo(destinationColumn)
    }

    @Test
    fun `creating test`() {
        // when
        val man = Man(Color.BLACK, 4, 4, board)

        // then
        assertThat(man.color).isEqualTo(Color.BLACK)
    }

    @Test
    fun `isMan test`() {
        // given
        val underTest = Man(Color.WHITE, 4, 4, board)

        // when
        val actual = underTest.isMan()

        // then
        assertThat(actual).isTrue()
    }

    @Test
    fun `isKing test`() {
        val underTest = Man(Color.WHITE, 4, 4, board)

        // when
        val actual = underTest.isKing()

        // then
        assertThat(actual).isFalse()
    }

    @ParameterizedTest
    @CsvSource("WHITE,piece white-man", "BLACK,piece black-man")
    fun `getCssClass test`(color: Color, expected: String) {
        // given
        val underTest = Man(color, 4, 4, board)

        // when
        val actual = underTest.getCssClass()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "4, 4, 5, 5, 1",
        "5, 5, 4, 4, 1",
        "1, 1, 8, 8, 7",
        "8, 8, 1, 1, 7",
        "1, 1, 3, 1, -1",
        "3, 1, 1, 1, -1",
    )
    fun `diff test`(firstColumn: Int, firstRow: Int, secondColumn: Int, secondRow: Int, expected: Int) {
        // given
        val underTest = Man(Color.WHITE, firstRow, firstColumn, board)
        board.board[firstRow - 1][firstColumn - 1] = underTest

        // when
        val actual = underTest.diff(secondRow to secondColumn)

        // then
        assertThat(actual).isEqualTo(expected)
    }
}
