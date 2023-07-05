package com.checkers.board;

import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class ManTest {

    private Piece underTest;

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsWhiteSource")
    void testIsAbleToMoveToWhileManIsWhite(int row, int column, boolean expected) throws CheckersException {
        CheckerBoard board = new CheckerBoard();
        clearBoard(board);

        underTest = new Man(Color.WHITE);
        var cell = board.getCell(4, 4);
        cell.setPiece(underTest);

        var currentCell = board.getCell(row, column);
        var actual = underTest.isAbleToMoveTo(currentCell);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> testIsAbleToMoveToWhileManIsWhiteSource() {
        List<Arguments> arguments = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                var expected = 4 - row == -1 && Math.abs(4 - column) == 1 && (((row + column) & 1) == 0);
                arguments.add(Arguments.of(row, column, expected));
            }
        }

        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsBlackSource")
    void testIsAbleToMoveToWhileManIsBlack(int row, int column, boolean expected) throws CheckersException {
        CheckerBoard board = new CheckerBoard();
        clearBoard(board);

        underTest = new Man(Color.BLACK);
        var cell = board.getCell(4, 4);
        cell.setPiece(underTest);

        var currentCell = board.getCell(row, column);
        var actual = underTest.isAbleToMoveTo(currentCell);
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> testIsAbleToMoveToWhileManIsBlackSource() {
        List<Arguments> arguments = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int column = 1; column <= 8; column++) {
                var expected = 4 - row == 1 && Math.abs(4 - column) == 1 && (((row + column) & 1) == 0);
                arguments.add(Arguments.of(row, column, expected));
            }
        }

        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsWhiteAndCellIsBusySource")
    void testIsAbleToMoveToWhileManIsWhiteAndCellIsBusy(int row, int column) throws CheckersException {
        CheckerBoard board = new CheckerBoard();
        clearBoard(board);

        underTest = new Man(Color.WHITE);
        var cell = board.getCell(4, 4);
        cell.setPiece(underTest);

        var cellTo = board.getCell(row, column);
        cellTo.setPiece(new Man(Color.WHITE));
        var actual = underTest.isAbleToMoveTo(cellTo);

        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> testIsAbleToMoveToWhileManIsWhiteAndCellIsBusySource() {
        return Stream.of(
                Arguments.of(5, 3),
                Arguments.of(5, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsBlackAndCellIsBusySource")
    void testIsAbleToMoveToWhileManIsBlackAndCellIsBusy(int row, int column) throws CheckersException {
        CheckerBoard board = new CheckerBoard();
        clearBoard(board);

        underTest = new Man(Color.BLACK);
        var cell = board.getCell(4, 4);
        cell.setPiece(underTest);

        var cellTo = board.getCell(row, column);
        cellTo.setPiece(new Man(Color.BLACK));
        var actual = underTest.isAbleToMoveTo(cellTo);

        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> testIsAbleToMoveToWhileManIsBlackAndCellIsBusySource() {
        return Stream.of(
                Arguments.of(3, 3),
                Arguments.of(3, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsWhiteAndMustEatEnemySource")
    void testIsAbleToMoveToWhileManIsWhiteAndMustEatEnemy(int row, int column, int enemyRow, int enemyCol) throws CheckersException {
        CheckerBoard board = new CheckerBoard();
        clearBoard(board);

        underTest = new Man(Color.BLACK);
        var cell = board.getCell(4, 4);
        cell.setPiece(underTest);

        var cellWithEnemy = board.getCell(enemyRow, enemyCol);
        cellWithEnemy.setPiece(new Man(Color.WHITE));

        var cellTo = board.getCell(row, column);
        var actual = underTest.isAbleToMoveTo(cellTo);

        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> testIsAbleToMoveToWhileManIsWhiteAndMustEatEnemySource() {
        return Stream.of(
                Arguments.of(5, 3, 5, 5),
                Arguments.of(5, 3, 3, 5),
                Arguments.of(5, 3, 3, 3),
                Arguments.of(5, 5, 5, 3),
                Arguments.of(5, 5, 3, 5),
                Arguments.of(5, 5, 3, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsBlackAndMustEatEnemySource")
    void testIsAbleToMoveToWhileManIsBlackAndMustEatEnemy(int row, int column, int enemyRow, int enemyCol) throws CheckersException {
        CheckerBoard board = new CheckerBoard();
        clearBoard(board);

        underTest = new Man(Color.WHITE);
        var cell = board.getCell(4, 4);
        cell.setPiece(underTest);

        var cellWithEnemy = board.getCell(enemyRow, enemyCol);
        cellWithEnemy.setPiece(new Man(Color.BLACK));

        var cellTo = board.getCell(row, column);
        var actual = underTest.isAbleToMoveTo(cellTo);

        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> testIsAbleToMoveToWhileManIsBlackAndMustEatEnemySource() {
        return Stream.of(
                Arguments.of(3, 3, 5, 5),
                Arguments.of(3, 3, 3, 5),
                Arguments.of(3, 3, 5, 3),
                Arguments.of(3, 5, 5, 3),
                Arguments.of(3, 5, 5, 5),
                Arguments.of(3, 5, 3, 3)
        );
    }

    private void clearBoard(CheckerBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var cell = board.getCell(i, j);
                cell.setPiece(null);
            }
        }
    }
}
