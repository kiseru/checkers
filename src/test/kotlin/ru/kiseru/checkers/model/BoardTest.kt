package ru.kiseru.checkers.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.error.ShouldBeExactlyInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.error.ChessError
import ru.kiseru.checkers.exception.CellIsBusyException
import ru.kiseru.checkers.exception.PieceException
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class BoardTest {

    private lateinit var underTest: Board

    @BeforeEach
    fun setUp() {
        underTest = Board(UUID.randomUUID())
    }

    @Test
    fun `makeTurn test when user can eat`() {
        // given
        underTest.board[2][2] = Piece(Color.WHITE, ManStrategy)
        underTest.board[3][3] = Piece(Color.BLACK, ManStrategy)

        val sourcePiece = underTest.board[2][2]

        // when
        val actual = underTest.makeTurn(Color.WHITE, 3 to 3, 5 to 5)

        // then
        assertThat(actual.isRight()).isTrue()
        assertThat(underTest.board[2][2]).isNull()
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isSameAs(sourcePiece)
    }

    @Test
    fun `makeTurn test when destination cell is busy`() {
        // given
        underTest.board[1][1] = Piece(Color.WHITE, ManStrategy)
        underTest.board[2][2] = Piece(Color.WHITE, ManStrategy)

        // when & then
        assertThatExceptionOfType(CellIsBusyException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, 2 to 2, 3 to 3) }
    }

    @Test
    fun `makeTurn test when user can move`() {
        // given
        underTest.board[2][2] = Piece(Color.WHITE, ManStrategy)

        // when
        underTest.makeTurn(Color.WHITE, 3 to 3, 4 to 4)

        // then
        assertThat(underTest.board[2][2]).isNull()
        assertThat(underTest.board[3][3]).isNotNull
    }

    @Test
    fun `analyze test when user cannot eat`() {
        // when
        val actual = underTest.analyze(Color.WHITE)

        // then
        assertThat(actual).isEqualTo(false)
    }



    @Test
    fun `makeTurn test when source cell hasn't piece`() {
        // when
        val actual = underTest.makeTurn(Color.WHITE, 2 to 2, 3 to 3)

        // then
        assertThat(actual.isLeft()).isTrue()
        actual.onLeft {
            assertThat(it).isExactlyInstanceOf(ChessError.EmptyCell::class.java)
            if (it !is ChessError.EmptyCell) {
                fail {
                    ShouldBeExactlyInstanceOf.shouldBeExactlyInstance(it, ChessError.EmptyCell::class.java)
                        .create()
                }
            }
            assertThat(it.cell).isEqualTo(2 to 2)
        }
    }

    @Test
    fun `makeTurn test when source cell hasn't player piece`() {
        underTest.board[1][1] = Piece(Color.BLACK, ManStrategy)

        // when & then
        assertThatExceptionOfType(PieceException::class.java)
            .isThrownBy { underTest.makeTurn(Color.WHITE, 2 to 2, 3 to 3) }
    }

    @Test
    fun `makeTurn test when can move`() {
        val piece = Piece(Color.WHITE, ManStrategy)
        underTest.board[3][3] = piece

        // when
        val actual = underTest.makeTurn(Color.WHITE, 4 to 4, 5 to 5)

        // then
        assertThat(actual.isRight()).isTrue()
        actual.onRight {
            assertThat(it).isFalse()
        }
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isSameAs(piece)
    }

    @Test
    fun `makeTurn test when can eat`() {
        val piece = Piece(Color.WHITE, ManStrategy)
        underTest.board[3][3] = piece

        underTest.board[4][4] = Piece(Color.BLACK, ManStrategy)

        // when
        val actual = underTest.makeTurn(Color.WHITE, 4 to 4, 6 to 6)

        // then
        assertThat(actual.isRight()).isTrue()
        actual.onRight {
            assertThat(it).isFalse()
        }
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isNull()
        assertThat(underTest.board[5][5]).isSameAs(piece)
    }

    @Test
    fun `makeTurn test when can eat two pieces`() {
        val piece = Piece(Color.WHITE, ManStrategy)
        underTest.board[3][3] = piece

        underTest.board[4][4] = Piece(Color.BLACK, ManStrategy)
        underTest.board[6][6] = Piece(Color.BLACK, ManStrategy)

        // when
        val actual = underTest.makeTurn(Color.WHITE, 4 to 4, 6 to 6)

        // then
        assertThat(actual.isRight()).isTrue()
        actual.onRight {
            assertThat(it).isTrue()
        }
        assertThat(underTest.board[3][3]).isNull()
        assertThat(underTest.board[4][4]).isNull()
        assertThat(underTest.board[5][5]).isSameAs(piece)
    }
}
