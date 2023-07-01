package com.checkers.board;

import com.checkers.utils.Colour;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.Mockito.mock;

class CellTest {

    @Test
    void testCreatingBlackCell() {
        var row = 0;
        var col = 0;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        assertThat(cell.getCol()).isEqualTo(col);
        assertThat(cell.getRow()).isEqualTo(row);
        assertThat(cell.getBoard()).isEqualTo(board);
        assertThat(cell.getColour()).isEqualTo(Colour.BLACK);
        assertThat(cell.getPiece()).isNull();
    }

    @Test
    void testCreatingWhiteCell() {
        var row = 0;
        var col = 1;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        assertThat(cell.getCol()).isEqualTo(col);
        assertThat(cell.getRow()).isEqualTo(row);
        assertThat(cell.getBoard()).isEqualTo(board);
        assertThat(cell.getColour()).isEqualTo(Colour.WHITE);
        assertThat(cell.getPiece()).isNull();
    }

    @Test
    void testSettingPieceWhileItIsNull() {
        var row = 0;
        var col = 0;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        assertThatNoException()
                .isThrownBy(() -> cell.setPiece(null));
        assertThat(cell.getPiece()).isNull();
    }

    @Test
    void testSettingPieceWhileItIsNotNull() {
        var row = 0;
        var col = 0;
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);
        var piece = mock(Piece.class);

        cell.setPiece(piece);

        assertThat(cell.getPiece()).isEqualTo(piece);
    }

    @ParameterizedTest
    @MethodSource("gettingMapSource")
    void testGettingMap(int col, int row, String result) {
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        var map = cell.getMap();

        assertThat(map).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("gettingMapSource")
    void testToString(int col, int row, String result) {
        var board = mock(CheckerBoard.class);
        var cell = new Cell(row, col, board);

        var map = cell.toString();

        assertThat(map).isEqualTo(result);
    }

    @ParameterizedTest
    @MethodSource("testDiffSource")
    void testDiff(int firstCol, int firstRow, int secondCol, int secondRow, int diff) {
        var board = mock(CheckerBoard.class);
        var firstCell = new Cell(firstCol, firstRow, board);
        var secondCell = new Cell(secondCol, secondRow, board);

        var diff1 = firstCell.diff(secondCell);
        assertThat(diff1).isEqualTo(diff);

        var diff2 = secondCell.diff(firstCell);
        assertThat(diff2).isEqualTo(diff);
    }

    @ParameterizedTest
    @MethodSource("testGetNearSource")
    void testGetNear(int diffRow, int diffCol, String foundCell) {
        var board = new CheckerBoard();
        var cell = new Cell(5, 5, board);

        var nearCell = cell.getNear(diffRow, diffCol);

        assertThat(nearCell.toString()).isEqualTo(foundCell);
    }

    @ParameterizedTest
    @MethodSource("testGetNearNonExistingCellSource")
    void testGetNearNonExistingCell(int diffRow, int diffCol) {
        var board = new CheckerBoard();
        var cell = new Cell(5, 5, board);

        assertThatExceptionOfType(ArrayIndexOutOfBoundsException.class)
                .isThrownBy(() -> cell.getNear(diffRow, diffCol));
    }

    @ParameterizedTest
    @MethodSource("testBetweenSource")
    void testBetween(int firstCol, int firstRow, int secondCol, int secondRow, String foundCell) {
        var board = new CheckerBoard();
        var firstCell = new Cell(firstCol, firstRow, board);
        var secondCell = new Cell(secondCol, secondRow, board);

        var between1 = firstCell.between(secondCell, board);
        assertThat(between1.toString()).isEqualTo(foundCell);

        var between2 = secondCell.between(firstCell, board);
        assertThat(between2.toString()).isEqualTo(foundCell);
    }

    static Stream<Arguments> gettingMapSource() {
        return Stream.of(
                Arguments.of(1, 1, "a1"),
                Arguments.of(1, 2, "a2"),
                Arguments.of(1, 3, "a3"),
                Arguments.of(1, 4, "a4"),
                Arguments.of(1, 5, "a5"),
                Arguments.of(1, 6, "a6"),
                Arguments.of(1, 7, "a7"),
                Arguments.of(1, 8, "a8"),
                Arguments.of(2, 1, "b1"),
                Arguments.of(2, 2, "b2"),
                Arguments.of(2, 3, "b3"),
                Arguments.of(2, 4, "b4"),
                Arguments.of(2, 5, "b5"),
                Arguments.of(2, 6, "b6"),
                Arguments.of(2, 7, "b7"),
                Arguments.of(2, 8, "b8"),
                Arguments.of(3, 1, "c1"),
                Arguments.of(3, 2, "c2"),
                Arguments.of(3, 3, "c3"),
                Arguments.of(3, 4, "c4"),
                Arguments.of(3, 5, "c5"),
                Arguments.of(3, 6, "c6"),
                Arguments.of(3, 7, "c7"),
                Arguments.of(3, 8, "c8"),
                Arguments.of(4, 1, "d1"),
                Arguments.of(4, 2, "d2"),
                Arguments.of(4, 3, "d3"),
                Arguments.of(4, 4, "d4"),
                Arguments.of(4, 5, "d5"),
                Arguments.of(4, 6, "d6"),
                Arguments.of(4, 7, "d7"),
                Arguments.of(4, 8, "d8"),
                Arguments.of(5, 1, "e1"),
                Arguments.of(5, 2, "e2"),
                Arguments.of(5, 3, "e3"),
                Arguments.of(5, 4, "e4"),
                Arguments.of(5, 5, "e5"),
                Arguments.of(5, 6, "e6"),
                Arguments.of(5, 7, "e7"),
                Arguments.of(5, 8, "e8"),
                Arguments.of(6, 1, "f1"),
                Arguments.of(6, 2, "f2"),
                Arguments.of(6, 3, "f3"),
                Arguments.of(6, 4, "f4"),
                Arguments.of(6, 5, "f5"),
                Arguments.of(6, 6, "f6"),
                Arguments.of(6, 7, "f7"),
                Arguments.of(6, 8, "f8"),
                Arguments.of(7, 1, "g1"),
                Arguments.of(7, 2, "g2"),
                Arguments.of(7, 3, "g3"),
                Arguments.of(7, 4, "g4"),
                Arguments.of(7, 5, "g5"),
                Arguments.of(7, 6, "g6"),
                Arguments.of(7, 7, "g7"),
                Arguments.of(7, 8, "g8"),
                Arguments.of(8, 1, "h1"),
                Arguments.of(8, 2, "h2"),
                Arguments.of(8, 3, "h3"),
                Arguments.of(8, 4, "h4"),
                Arguments.of(8, 5, "h5"),
                Arguments.of(8, 6, "h6"),
                Arguments.of(8, 7, "h7"),
                Arguments.of(8, 8, "h8")
        );
    }

    static Stream<Arguments> testDiffSource() {
        return Stream.of(
                Arguments.of(4, 4, 5, 5, 1),
                Arguments.of(1, 1, 8, 8, 7),
                Arguments.of(1, 1, 3, 1, -1)
        );
    }

    static Stream<Arguments> testGetNearSource() {
        return Stream.of(
                Arguments.of(1, 1, "f6"),
                Arguments.of(-1, -1, "d4")
        );
    }

    static Stream<Arguments> testGetNearNonExistingCellSource() {
        return Stream.of(
                Arguments.of(4, 4, "f6"),
                Arguments.of(-5, -5, "d4")
        );
    }

    static Stream<Arguments> testBetweenSource() {
        return Stream.of(
                Arguments.of(4, 4, 5, 5, "d4"),
                Arguments.of(1, 1, 8, 8, "d4"),
                Arguments.of(5, 5, 7, 7, "f6"),
                Arguments.of(3, 3, 5, 3, "c4")
        );
    }
}
