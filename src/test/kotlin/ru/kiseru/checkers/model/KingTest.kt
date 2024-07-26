package ru.kiseru.checkers.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.exception.CannotEatException
import ru.kiseru.checkers.exception.CannotMoveException
import ru.kiseru.checkers.exception.MustEatException
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class KingTest {

    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        board = Board(UUID.randomUUID())
        for (cells in board.board) {
            for (i in cells.indices) {
                cells[i] = null
            }
        }
    }

    @Test
    fun `move test when can eat`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest
        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(MustEatException::class.java).isThrownBy { underTest.move(board, 3 to 3, 4 to 4) }
    }

    @Test
    fun `move test when can't move`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest
        underTest.isCanEat = false
        underTest.isCanMove = false

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java).isThrownBy { underTest.move(board, 3 to 3, 4 to 4) }
    }

    @Test
    fun `move test when can't move to destination cell`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest

        underTest.isCanEat = false
        underTest.isCanMove = true

        // when & then
        assertThatExceptionOfType(CannotMoveException::class.java).isThrownBy { underTest.move(board, 3 to 3, 5 to 3) }
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
        val underTest = King(color)
        underTest.isCanMove = true
        board.board[sourceRow - 1][sourceColumn - 1] = underTest

        val enemyColor = when (color) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        val enemyPiece = King(enemyColor)
        board.board[enemyRow - 1][enemyColumn - 1] = enemyPiece

        // when
        underTest.move(board, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isSameAs(enemyPiece)
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(underTest)
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,4,4,8,8",
        "WHITE,4,4,1,1",
        "WHITE,4,4,1,7",
        "WHITE,4,4,7,1",
        "BLACK,4,4,8,8",
        "BLACK,4,4,1,1",
        "BLACK,4,4,1,7",
        "BLACK,4,4,7,1",
    )
    fun `move test when can move to destination cell`(
        sourceColor: Color,
        sourceRow: Int,
        sourceColumn: Int,
        destinationRow: Int,
        destinationColumn: Int,
    ) {
        // given
        val underTest = King(sourceColor)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest
        underTest.isCanEat = false
        underTest.isCanMove = true

        // when
        underTest.move(board, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(underTest)
    }

    @Test
    fun `eat test when can't eat`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest
        underTest.isCanEat = false

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java).isThrownBy { underTest.eat(board, 3 to 3, 8 to 8) }
    }

    @Test
    fun `eat test when can't eat to destination cell`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest
        underTest.isCanEat = true

        board.board[7][7] = King(Color.WHITE)

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java).isThrownBy { underTest.eat(board, 3 to 3, 8 to 8) }
    }

    @Test
    fun `eat test when there is no sacrificed piece source cell and destination cell`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest
        underTest.isCanEat = true

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java).isThrownBy { underTest.eat(board, 3 to 3, 8 to 8) }
    }

    @Test
    fun `eat test when there is a player piece between source cell and destination cell`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest
        underTest.isCanEat = true

        board.board[3][3] = King(Color.WHITE)

        // when & then
        assertThatExceptionOfType(CannotEatException::class.java).isThrownBy { underTest.eat(board, 3 to 3, 8 to 8) }
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE,4,4,6,6,8,8",
        "WHITE,4,4,2,2,1,1",
        "WHITE,4,4,2,6,1,7",
        "WHITE,4,4,6,2,7,1",
        "BLACK,4,4,6,6,8,8",
        "BLACK,4,4,2,2,1,1",
        "BLACK,4,4,2,6,1,7",
        "BLACK,4,4,6,2,7,1",
    )
    fun `eat test when there is an enemy piece between source cell and destination cell`(
        sourceColor: Color,
        sourceRow: Int,
        sourceColumn: Int,
        enemyRow: Int,
        enemyColumn: Int,
        destinationRow: Int,
        destinationColumn: Int,
    ) {
        // given
        val underTest = King(sourceColor)
        board.board[sourceRow - 1][sourceColumn - 1] = underTest
        underTest.isCanEat = true

        val enemyColor = when (sourceColor) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        board.board[enemyRow - 1][enemyColumn - 1] = King(enemyColor)

        // when
        underTest.eat(board, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isNull()
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(underTest)
    }

    @ParameterizedTest
    @CsvSource(
        "WHITE, #",
        "BLACK, &",
    )
    fun `toString test`(color: Color, expected: String) {
        // given
        val underTest = King(color)

        // when
        val actual = underTest.toString()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `analyzeAbilityOfMove test when there are no moves`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[0][0] = underTest

        board.board[1][1] = King(Color.WHITE)

        // when
        underTest.analyzeAbilityOfMove(board, 1 to 1)

        // then
        assertThat(underTest.isCanMove).isFalse()
    }

    @Test
    fun `analyzeAbilityOfMove test when there's an available move`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest

        // when
        underTest.analyzeAbilityOfMove(board, 3 to 3)

        // then
        assertThat(underTest.isCanMove).isTrue()
    }

    @Test
    fun `analyzeAbilityOfEat test when there are no moves`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest

        // when
        underTest.analyzeAbilityOfEat(board, 3 to 3)

        // then
        assertThat(underTest.isCanEat).isFalse()
    }

    @Test
    fun `analyzeAbilityOfEat test when there's an available move`() {
        // given
        val underTest = King(Color.WHITE)
        board.board[2][2] = underTest

        board.board[3][3] = King(Color.BLACK)

        // when
        underTest.analyzeAbilityOfEat(board, 3 to 3)

        // then
        assertThat(underTest.isCanEat).isTrue()
    }
}
