package ru.kiseru.checkers.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.exception.MustEatException
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class ManStrategyTest {

    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        board = Board(UUID.randomUUID())
    }

    @Test
    fun `analyzeAbilityOfMove test`() {
        // given
        val piece = Piece(Color.WHITE, ManStrategy)
        board.board[2][2] = piece

        // when
        val actual = ManStrategy.analyzeAbilityOfMove(board, piece, 3 to 3)

        // then
        assertThat(actual).isTrue()
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
        val piece = Piece(color, ManStrategy)
        board.board[row - 1][column - 1] = piece

        // when
        val actual = ManStrategy.analyzeAbilityOfMove(board, piece, row to column)

        // then
        assertThat(actual).isFalse()
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
        val piece = Piece(sourceColor, ManStrategy)
        board.board[sourceRow - 1][sourceColumn - 1] = piece

        board.board[targetRow - 1][targetColumn - 1] = Piece(targetColor, ManStrategy)

        // when
        val actual = ManStrategy.analyzeAbilityOfEat(board, piece, sourceRow to sourceColumn)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `move test when man can eat`() {
        // given
        val piece = Piece(Color.WHITE, ManStrategy)
        board.board[2][2] = piece
        piece.isCanEat = true

        // when & then
        assertThatExceptionOfType(MustEatException::class.java)
            .isThrownBy { ManStrategy.move(board, piece, 3 to 3, 4 to 4) }
    }

    @Test
    fun `move test when man can't move`() {
        // given
        val piece = Piece(Color.WHITE, ManStrategy)
        board.board[2][2] = piece
        piece.isCanEat = false
        piece.isCanMove = false

        // when
        val actual = ManStrategy.move(board, piece, 3 to 3, 4 to 4)

        // then
        assertThat(actual.isLeft()).isTrue()
        actual.onLeft { (source, destination) ->
            assertThat(source).isEqualTo(3 to 3)
            assertThat(destination).isEqualTo(4 to 4)
        }
    }

    @Test
    fun `move test when man can't move to`() {
        // given
        val piece = Piece(Color.WHITE, ManStrategy)
        board.board[2][2] = piece
        piece.isCanEat = false
        piece.isCanMove = true

        // when
        val actual = ManStrategy.move(board, piece, 3 to 3, 5 to 3)

        // then
        assertThat(actual.isLeft()).isTrue()
        actual.onLeft { (source, destination) ->
            assertThat(source).isEqualTo(3 to 3)
            assertThat(destination).isEqualTo(5 to 3)
        }
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
        val piece = Piece(color, ManStrategy)
        piece.isCanMove = true
        board.board[sourceRow - 1][sourceColumn - 1] = piece

        val enemyColor = when(color) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        val enemyPiece = Piece(enemyColor, ManStrategy)
        board.board[enemyRow - 1][enemyColumn - 1] = enemyPiece

        // when
        ManStrategy.move(board, piece, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isSameAs(enemyPiece)
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(piece)
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
        val piece = Piece(color, ManStrategy)
        board.board[sourceRow - 1][sourceColumn - 1] = piece
        piece.isCanEat = false
        piece.isCanMove = true

        // when
        ManStrategy.move(board, piece, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(piece)
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
        val piece = Piece(color, ManStrategy)
        board.board[sourceRow - 1][sourceColumn - 1] = piece
        piece.isCanEat = false
        piece.isCanMove = true

        // when
        ManStrategy.move(board, piece, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        val destinationPiece = board.board[destinationRow - 1][destinationColumn - 1]
        assertThat(destinationPiece?.pieceStrategy).isInstanceOf(KingStrategy::class.java)
        assertThat(destinationPiece?.color).isEqualTo(color)
    }

    @Test
    fun `eat test when piece can't eat`() {
        // given
        val piece = Piece(Color.WHITE, ManStrategy)
        board.board[2][2] = piece
        piece.isCanEat = false

        // when
        val actual = ManStrategy.eat(board, piece, 3 to 3, 4 to 4)

        // then
        assertThat(actual.isLeft()).isTrue()
        actual.onLeft { (source, destination) ->
            assertThat(source).isEqualTo(3 to 3)
            assertThat(destination).isEqualTo(4 to 4)
        }
    }

    @Test
    fun `eat test when piece can't eat at some destination`() {
        // given
        val piece = Piece(Color.WHITE, ManStrategy)
        board.board[2][2] = piece
        piece.isCanEat = true

        // when
        val actual = ManStrategy.eat(board, piece, 3 to 3, 4 to 4)

        // then
        assertThat(actual.isLeft()).isTrue()
        actual.onLeft { (source, destination) ->
            assertThat(source).isEqualTo(3 to 3)
            assertThat(destination).isEqualTo(4 to 4)
        }
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
        val piece = Piece(sourceColor, ManStrategy)
        board.board[sourceRow - 1][sourceColumn - 1] = piece
        piece.isCanEat = true

        val enemyColor = when(sourceColor) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        board.board[enemyRow - 1][enemyColumn - 1] = Piece(enemyColor, ManStrategy)

        // when
        val actual = ManStrategy.eat(board, piece, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(actual.isRight()).isTrue()
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isNull()
        assertThat(board.board[destinationRow - 1][destinationColumn - 1]).isSameAs(piece)
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
        val piece = Piece(color, ManStrategy)
        board.board[sourceRow - 1][sourceColumn - 1] = piece
        piece.isCanEat = true

        val enemyColor = when (color) {
            Color.WHITE -> Color.BLACK
            Color.BLACK -> Color.WHITE
        }
        board.board[enemyRow - 1][enemyColumn - 1] = Piece(enemyColor, ManStrategy)

        // when
        val actual = ManStrategy.eat(board, piece, sourceRow to sourceColumn, destinationRow to destinationColumn)

        // then
        assertThat(actual.isRight()).isTrue()
        assertThat(board.board[sourceRow - 1][sourceColumn - 1]).isNull()
        assertThat(board.board[enemyRow - 1][enemyColumn - 1]).isNull()
        val destinationPiece = board.board[destinationRow - 1][destinationColumn - 1]
        assertThat(destinationPiece?.pieceStrategy).isInstanceOf(KingStrategy::class.java)
        assertThat(destinationPiece?.color).isEqualTo(color)
    }

    @Test
    fun `creating test`() {
        // when
        val man = Piece(Color.BLACK, ManStrategy)

        // then
        assertThat(man.color).isEqualTo(Color.BLACK)
    }
}
