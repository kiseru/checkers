package ru.kiseru.checkers.board

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.springframework.test.util.ReflectionTestUtils
import ru.kiseru.checkers.utils.Color

@ExtendWith(MockitoExtension::class)
class CellTest {

    @Mock
    private lateinit var board: Board

    private lateinit var underTest: Cell

    @BeforeEach
    fun setUp() {
        underTest = Cell(5, 5, board)
    }


    @Test
    fun `black cell creation test`() {
        // given
        ReflectionTestUtils.setField(underTest, "row", 1)
        ReflectionTestUtils.setField(underTest, "column", 1)

        // when
        val actual = underTest.color

        // then
        assertThat(actual).isEqualTo(Color.BLACK)
    }

    @Test
    fun `white cell creation test`() {
        // given
        ReflectionTestUtils.setField(underTest, "row", 1)
        ReflectionTestUtils.setField(underTest, "column", 2)

        // when
        val actual = underTest.color

        // then
        assertThat(actual).isEqualTo(Color.WHITE)
    }

    @Test
    fun `piece setting test when it's null`() {
        // when
        underTest.piece = null

        // then
        assertThat(underTest.piece).isNull()
    }

    @Test
    fun `piece setting test when it's not null`() {
        // given
        val piece = mock<Piece>()

        // when
        underTest.piece = piece

        // then
        val actual = underTest.piece
        assertThat(actual).isSameAs(piece)
    }

    @ParameterizedTest
    @CsvSource(
        "1, 1, a1",
        "1, 2, a2",
        "1, 3, a3",
        "1, 4, a4",
        "1, 5, a5",
        "1, 6, a6",
        "1, 7, a7",
        "1, 8, a8",
        "2, 1, b1",
        "2, 2, b2",
        "2, 3, b3",
        "2, 4, b4",
        "2, 5, b5",
        "2, 6, b6",
        "2, 7, b7",
        "2, 8, b8",
        "3, 1, c1",
        "3, 2, c2",
        "3, 3, c3",
        "3, 4, c4",
        "3, 5, c5",
        "3, 6, c6",
        "3, 7, c7",
        "3, 8, c8",
        "4, 1, d1",
        "4, 2, d2",
        "4, 3, d3",
        "4, 4, d4",
        "4, 5, d5",
        "4, 6, d6",
        "4, 7, d7",
        "4, 8, d8",
        "5, 1, e1",
        "5, 2, e2",
        "5, 3, e3",
        "5, 4, e4",
        "5, 5, e5",
        "5, 6, e6",
        "5, 7, e7",
        "5, 8, e8",
        "6, 1, f1",
        "6, 2, f2",
        "6, 3, f3",
        "6, 4, f4",
        "6, 5, f5",
        "6, 6, f6",
        "6, 7, f7",
        "6, 8, f8",
        "7, 1, g1",
        "7, 2, g2",
        "7, 3, g3",
        "7, 4, g4",
        "7, 5, g5",
        "7, 6, g6",
        "7, 7, g7",
        "7, 8, g8",
        "8, 1, h1",
        "8, 2, h2",
        "8, 3, h3",
        "8, 4, h4",
        "8, 5, h5",
        "8, 6, h6",
        "8, 7, h7",
        "8, 8, h8",
    )
    fun `toString test`(column: Int, row: Int, expected: String) {
        // given
        ReflectionTestUtils.setField(underTest, "column", column)
        ReflectionTestUtils.setField(underTest, "row", row)

        // when
        val actual = underTest.toString()

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
        ReflectionTestUtils.setField(underTest, "row", firstRow)
        ReflectionTestUtils.setField(underTest, "column", firstColumn)

        val second = Cell(secondRow, secondColumn, board)

        // when
        val actual = underTest.diff(second)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "1, 1, f6",
        "-1, -1, d4"
    )
    fun `getNear test when the desired cell exists`(rowDiff: Int, columnDiff: Int, expected: String) {
        // given
        val row = rowDiff + 5
        val column = columnDiff + 5
        given(board.getCell(eq(row), eq(column))).willReturn(Cell(row, column, board))

        // when
        val actual = underTest.getNear(rowDiff, columnDiff)

        // then
        assertThat(actual.toString()).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "4, 4",
        "-5, -5",
    )
    fun `getNear test when the desired cell doesn't exist`(rowDiff: Int, columnDiff: Int) {
        // when & then
        assertThatExceptionOfType(ArrayIndexOutOfBoundsException::class.java)
            .isThrownBy { underTest.getNear(rowDiff, columnDiff) }
    }

    @ParameterizedTest
    @CsvSource(
        "4, 4, 5, 5, d4",
        "5, 5, 4, 4, d4",
        "1, 1, 8, 8, d4",
        "8, 8, 1, 1, d4",
        "5, 5, 7, 7, f6",
        "7, 7, 5, 5, f6",
        "3, 3, 5, 3, c4",
        "5, 3, 3, 3, c4",
    )
    fun `between test`(firstRow: Int, firstColumn: Int, secondRow: Int, secondColumn: Int, expected: String) {
        // given
        ReflectionTestUtils.setField(underTest, "row", firstRow)
        ReflectionTestUtils.setField(underTest, "column", firstColumn)

        val cell = Cell(secondRow, secondColumn, board)

        val rowToFind = (firstRow + secondRow) / 2
        val columnToFind = (firstColumn + secondColumn) / 2
        given(board.getCell(eq(rowToFind), eq(columnToFind))).willReturn(Cell(rowToFind, columnToFind, board))

        // when
        val actual = underTest.between(cell)

        // then
        assertThat(actual.toString()).isEqualTo(expected)
    }
}