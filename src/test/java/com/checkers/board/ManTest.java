package com.checkers.board;

import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class ManTest {

    private static final int PIECE_CELL_COLUMN = 4;

    private static final int PIECE_CELL_ROW = 4;

    private Piece underTest;

    private CheckerBoard board;

    private Cell pieceCell;

    @BeforeEach
    void setUp() {
        board = new CheckerBoard();
        clearBoard(board);

        pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToMoveToWhileManIsWhiteSource")
    void testIsAbleToMoveToWhileManIsWhite(int row, int column, boolean expected) throws CheckersException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

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
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

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
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var destinationCell = board.getCell(row, column);
        destinationCell.setPiece(new Man(Color.WHITE));
        var actual = underTest.isAbleToMoveTo(destinationCell);

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
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

        var destinationCell = board.getCell(row, column);
        destinationCell.setPiece(new Man(Color.BLACK));
        var actual = underTest.isAbleToMoveTo(destinationCell);

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
    void testIsAbleToMoveToWhileManIsWhiteAndMustEatEnemy(
            int row,
            int column,
            int enemyRow,
            int enemyColumn
    ) throws CheckersException {
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

        var cellWithEnemy = board.getCell(enemyRow, enemyColumn);
        cellWithEnemy.setPiece(new Man(Color.WHITE));

        var destinationCell = board.getCell(row, column);
        var actual = underTest.isAbleToMoveTo(destinationCell);

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
    void testIsAbleToMoveToWhileManIsBlackAndMustEatEnemy(
            int row,
            int column,
            int enemyRow,
            int enemyColumn
    ) throws CheckersException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var cellWithEnemy = board.getCell(enemyRow, enemyColumn);
        cellWithEnemy.setPiece(new Man(Color.BLACK));

        var destinationCell = board.getCell(row, column);
        var actual = underTest.isAbleToMoveTo(destinationCell);

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

    @ParameterizedTest
    @MethodSource("testIsAbleToEatToWhileDiffIsNotEqualsTwoSource")
    void testIsAbleToEatToWhileDiffIsNotEqualsTwo(int destinationRow, int destinationColumn) throws CheckersException {
        var destinationCell = board.getCell(destinationRow, destinationColumn);

        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isFalse();
    }

    private static Stream<Arguments> testIsAbleToEatToWhileDiffIsNotEqualsTwoSource() {
        return Stream.of(
                Arguments.of(3, 3),
                Arguments.of(3, 5),
                Arguments.of(5, 5),
                Arguments.of(5, 3),
                Arguments.of(1, 3),
                Arguments.of(7, 1),
                Arguments.of(7, 7),
                Arguments.of(1, 7)
        );
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testIsAbleToEatToWhileDestinationCellIsNotEmpty(
            int destinationRow,
            int destinationColumn
    ) throws CheckersException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var destinationCell = board.getCell(destinationRow, destinationColumn);
        destinationCell.setPiece(new Man(Color.WHITE));
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testIsAbleToEatWhileThereIsNoSacrifice(int destinationRow, int destinationColumn) throws CheckersException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var destinationCell = board.getCell(destinationRow, destinationColumn);
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testIsAbleToEatWhileThereIsOwnCheckerAsSacrifice(
            int destinationRow,
            int destinationColumn
    ) throws CheckersException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var cellWithSacrifice = board.getCell(
                (PIECE_CELL_ROW + destinationRow) / 2,
                (PIECE_CELL_COLUMN + destinationColumn) / 2
        );
        cellWithSacrifice.setPiece(new Man(Color.WHITE));

        var destinationCell = board.getCell(destinationRow, destinationColumn);
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testIsAbleToEatWhileThereIsEnemyCheckerAsSacrifice(
            int destinationRow,
            int destinationColumn
    ) throws CheckersException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var cellWithSacrifice = board.getCell(
                (PIECE_CELL_ROW + destinationRow) / 2,
                (PIECE_CELL_COLUMN + destinationColumn) / 2
        );
        cellWithSacrifice.setPiece(new Man(Color.BLACK));

        var destinationCell = board.getCell(destinationRow, destinationColumn);
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionMoveOfWhiteMan")
    void testAnalyzeAbilityOfMoveWhileWhiteManHasOpportunityToMove(int notEmptyCellRow, int notEmptyCellColumn)
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var notEmptyCell = board.getCell(notEmptyCellRow, notEmptyCellColumn);
        notEmptyCell.setPiece(new Man(Color.WHITE));

        underTest.analyzeAbilityOfMove();

        var pieceClass = Piece.class;
        var canMoveField = pieceClass.getDeclaredField("canMove");
        canMoveField.setAccessible(true);
        var canMove = (Boolean) canMoveField.get(underTest);

        assertThat(canMove).isTrue();
    }

    private static Stream<Arguments> nextCellForActionMoveOfWhiteMan() {
        return Stream.of(
                Arguments.of(PIECE_CELL_ROW + 1, PIECE_CELL_COLUMN - 1),
                Arguments.of(PIECE_CELL_ROW + 1, PIECE_CELL_COLUMN + 1)
        );
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileWhiteManHasNoOpportunityToMove()
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        board.getCell(PIECE_CELL_ROW + 1, PIECE_CELL_COLUMN - 1)
                .setPiece(new Man(Color.WHITE));
        board.getCell(PIECE_CELL_ROW + 1, PIECE_CELL_COLUMN + 1)
                .setPiece(new Man(Color.WHITE));

        underTest.analyzeAbilityOfMove();

        var pieceClass = Piece.class;
        var canMoveField = pieceClass.getDeclaredField("canMove");
        canMoveField.setAccessible(true);
        var canMove = (Boolean) canMoveField.get(underTest);

        assertThat(canMove).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionMoveOfBlackMan")
    void testAnalyzeAbilityOfMoveWhileBlackManHasOpportunityToMove(int notEmptyCellRow, int notEmptyCellColumn)
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

        var notEmptyCell = board.getCell(notEmptyCellRow, notEmptyCellColumn);
        notEmptyCell.setPiece(new Man(Color.BLACK));

        underTest.analyzeAbilityOfMove();

        var pieceClass = Piece.class;
        var canMoveField = pieceClass.getDeclaredField("canMove");
        canMoveField.setAccessible(true);
        var canMove = (Boolean) canMoveField.get(underTest);

        assertThat(canMove).isTrue();
    }

    private static Stream<Arguments> nextCellForActionMoveOfBlackMan() {
        return Stream.of(
                Arguments.of(PIECE_CELL_ROW - 1, PIECE_CELL_COLUMN - 1),
                Arguments.of(PIECE_CELL_ROW - 1, PIECE_CELL_COLUMN + 1)
        );
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileBlackManHasNoOpportunityToMove()
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

        board.getCell(PIECE_CELL_ROW - 1, PIECE_CELL_COLUMN - 1)
                .setPiece(new Man(Color.BLACK));
        board.getCell(PIECE_CELL_ROW - 1, PIECE_CELL_COLUMN + 1)
                .setPiece(new Man(Color.BLACK));

        underTest.analyzeAbilityOfMove();

        var pieceClass = Piece.class;
        var canMoveField = pieceClass.getDeclaredField("canMove");
        canMoveField.setAccessible(true);
        var canMove = (Boolean) canMoveField.get(underTest);

        assertThat(canMove).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testAnalyzeAbilityOfEatWhileWhiteManHasOpportunityToEat(int destinationRow, int destinationColumn)
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        var cellWithSacrifice = board.getCell(
                (PIECE_CELL_ROW + destinationRow) / 2,
                (PIECE_CELL_COLUMN + destinationColumn) / 2
        );
        cellWithSacrifice.setPiece(new Man(Color.BLACK));

        underTest.analyzeAbilityOfEat();

        var pieceClass = Piece.class;
        var canEatField = pieceClass.getDeclaredField("canEat");
        canEatField.setAccessible(true);
        var canEat = (Boolean) canEatField.get(underTest);

        assertThat(canEat).isTrue();
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileWhiteManHasNoOpportunityToEat()
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.WHITE);
        pieceCell.setPiece(underTest);

        underTest.analyzeAbilityOfEat();

        var pieceClass = Piece.class;
        var canEatField = pieceClass.getDeclaredField("canEat");
        canEatField.setAccessible(true);
        var canEat = (Boolean) canEatField.get(underTest);

        assertThat(canEat).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testAnalyzeAbilityOfMoveWhileBlackManHasOpportunityToEat(int destinationRow, int destinationColumn)
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

        var cellWithSacrifice = board.getCell(
                (PIECE_CELL_ROW + destinationRow) / 2,
                (PIECE_CELL_COLUMN + destinationColumn) / 2
        );
        cellWithSacrifice.setPiece(new Man(Color.WHITE));

        underTest.analyzeAbilityOfEat();

        var pieceClass = Piece.class;
        var canEatField = pieceClass.getDeclaredField("canEat");
        canEatField.setAccessible(true);
        var canEat = (Boolean) canEatField.get(underTest);

        assertThat(canEat).isTrue();
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileBlackManHasNoOpportunityToEat()
            throws CheckersException, NoSuchFieldException, IllegalAccessException {
        underTest = new Man(Color.BLACK);
        pieceCell.setPiece(underTest);

        underTest.analyzeAbilityOfEat();

        var pieceClass = Piece.class;
        var canEatField = pieceClass.getDeclaredField("canEat");
        canEatField.setAccessible(true);
        var canEat = (Boolean) canEatField.get(underTest);

        assertThat(canEat).isFalse();
    }

    private static Stream<Arguments> nextCellForActionEat() {
        return Stream.of(
                Arguments.of(PIECE_CELL_ROW + 2, PIECE_CELL_COLUMN + 2),
                Arguments.of(PIECE_CELL_ROW - 2, PIECE_CELL_COLUMN + 2),
                Arguments.of(PIECE_CELL_ROW - 2, PIECE_CELL_COLUMN - 2),
                Arguments.of(PIECE_CELL_ROW + 2, PIECE_CELL_COLUMN - 2)
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
