package com.checkers.board;

import com.checkers.exceptions.CanEatException;
import com.checkers.exceptions.CanNotEatException;
import com.checkers.exceptions.CanNotMoveException;
import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.argThat;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.isNull;
import static org.mockito.BDDMockito.refEq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ManTest {

    private static final int PIECE_CELL_COLUMN = 4;

    private static final int PIECE_CELL_ROW = 4;

    @Mock
    private Man underTest;

    @Mock
    private CheckerBoard board;

    @Mock
    private Cell sourceCell;

    @Mock
    private Cell destinationCell;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "cell", sourceCell);
        ReflectionTestUtils.setField(sourceCell, "piece", underTest);
    }

    @Test
    void testIsAbleToMoveToWhileDestinationCellIsNotEmpty() {
        // given
        given(destinationCell.isEmpty()).willReturn(Boolean.FALSE);

        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileEnemyIsNearby() {
        // given
        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);

        var enemy = mock(Man.class);
        given(enemy.getColor()).willReturn(Color.BLACK);

        var cellWithEnemy = mock(Cell.class);
        given(cellWithEnemy.getPiece()).willReturn(enemy);

        given(board.getCell(anyInt(), anyInt())).willReturn(cellWithEnemy);

        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileDestinationDiffIsMoreThanOne() {
        // given
        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);

        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);

        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileCanMoveToDestination() {
        // given
        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);

        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        given(board.getCell(eq(PIECE_CELL_ROW + 1), eq(PIECE_CELL_COLUMN - 1))).willReturn(destinationCell);
        given(board.getCell(eq(PIECE_CELL_ROW + 1), eq(PIECE_CELL_COLUMN + 1))).willReturn(destinationCell);
        given(board.getCell(eq(PIECE_CELL_ROW - 1), eq(PIECE_CELL_COLUMN - 1))).willReturn(emptyCell);
        given(board.getCell(eq(PIECE_CELL_ROW - 1), eq(PIECE_CELL_COLUMN + 1))).willReturn(emptyCell);

        given(sourceCell.getCol()).willReturn(PIECE_CELL_COLUMN);
        given(sourceCell.getRow()).willReturn(PIECE_CELL_ROW);
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToEatToWhileDiffIsNotEqualsTwoSource")
    void testIsAbleToEatToWhileDiffIsNotEqualsTwo(int destinationRow, int destinationColumn) {
        var board = new CheckerBoard();
        clearBoard(board);

        var destinationCell = board.getCell(destinationRow, destinationColumn);

        var underTest = new Man(Color.WHITE);

        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
    void testIsAbleToEatToWhileDestinationCellIsNotEmpty(int destinationRow, int destinationColumn) {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
        pieceCell.setPiece(underTest);

        var destinationCell = board.getCell(destinationRow, destinationColumn);
        destinationCell.setPiece(new Man(Color.WHITE));
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testIsAbleToEatWhileThereIsNoSacrifice(int destinationRow, int destinationColumn) {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
        pieceCell.setPiece(underTest);

        var destinationCell = board.getCell(destinationRow, destinationColumn);
        var actual = underTest.isAbleToEatTo(destinationCell);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("nextCellForActionEat")
    void testIsAbleToEatWhileThereIsOwnCheckerAsSacrifice(int destinationRow, int destinationColumn) {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
    void testIsAbleToEatWhileThereIsEnemyCheckerAsSacrifice(int destinationRow, int destinationColumn) {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.BLACK);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.BLACK);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.WHITE);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.BLACK);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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
            throws NoSuchFieldException, IllegalAccessException {
        var underTest = new Man(Color.BLACK);

        var board = new CheckerBoard();
        clearBoard(board);
        var pieceCell = board.getCell(PIECE_CELL_ROW, PIECE_CELL_COLUMN);
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

    @Test
    void testMoveWhileCanEat() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.TRUE);
        willCallRealMethod()
                .given(man)
                .move(any());

        // then
        assertThatExceptionOfType(CanEatException.class)
                .isThrownBy(() -> man.move(null));
    }

    @Test
    void testMoveWhileCannotMove() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.FALSE);
        given(man.isCanMove()).willReturn(Boolean.FALSE);
        willCallRealMethod()
                .given(man)
                .move(any());

        // then
        assertThatExceptionOfType(CanNotMoveException.class)
                .isThrownBy(() -> man.move(null));
    }

    @Test
    void testMoveWhileCannotMoveTo() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.FALSE);
        given(man.isCanMove()).willReturn(Boolean.TRUE);
        given(man.isAbleToMoveTo(any())).willReturn(Boolean.FALSE);
        willCallRealMethod()
                .given(man)
                .move(any());

        // then
        assertThatExceptionOfType(CanNotMoveException.class)
                .isThrownBy(() -> man.move(null));
    }

    @Test
    void testMove() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.FALSE);
        given(man.isCanMove()).willReturn(Boolean.TRUE);
        given(man.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);

        var sourceCell = mock(Cell.class);
        willCallRealMethod().given(man).setCell(eq(sourceCell));
        man.setCell(sourceCell);

        var destinationCell = mock(Cell.class);
        willCallRealMethod().given(man).move(destinationCell);

        // when
        man.move(destinationCell);

        // then
        then(destinationCell).should(times(1)).setPiece(eq(man));
        then(sourceCell).should(times(1)).setPiece(isNull());
    }

    @Test
    void testMoveWhileWhitePieceBecomeKing() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.FALSE);
        given(man.isCanMove()).willReturn(Boolean.TRUE);
        given(man.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);

        ReflectionTestUtils.setField(man, "color", Color.WHITE);

        var sourceCell = mock(Cell.class);
        willCallRealMethod().given(man).setCell(eq(sourceCell));
        man.setCell(sourceCell);

        var destinationCell = mock(Cell.class);
        given(destinationCell.getRow()).willReturn(8);
        willCallRealMethod().given(man).move(destinationCell);

        // when
        man.move(destinationCell);

        // then
        then(sourceCell).should(times(1)).setPiece(isNull());
        then(destinationCell)
                .should(times(1))
                .setPiece(argThat(piece -> piece instanceof King && piece.getColor() == Color.WHITE));
    }

    @Test
    void testMoveWhileBlackPieceBecomeKing() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.FALSE);
        given(man.isCanMove()).willReturn(Boolean.TRUE);
        given(man.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);

        ReflectionTestUtils.setField(man, "color", Color.BLACK);

        var sourceCell = mock(Cell.class);
        willCallRealMethod().given(man).setCell(eq(sourceCell));
        man.setCell(sourceCell);

        var destinationCell = mock(Cell.class);
        given(destinationCell.getRow()).willReturn(1);
        willCallRealMethod().given(man).move(destinationCell);

        // when
        man.move(destinationCell);

        // then
        then(sourceCell).should(times(1)).setPiece(isNull());
        then(destinationCell)
                .should(times(1))
                .setPiece(argThat(piece -> piece instanceof King && piece.getColor() == Color.BLACK));
    }

    @Test
    void testEatWhileCannotEat() throws CheckersException {
        // given
        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.FALSE);
        willCallRealMethod().given(man).eat(any());

        // then
        assertThatExceptionOfType(CanNotEatException.class)
                .isThrownBy(() -> man.eat(null));
    }

    @Test
    void testEatWhileCannotEatAtSomeDestination() throws CheckersException {
        // given
        var destinationCell = mock(Cell.class);

        var man = mock(Man.class);
        given(man.isCanEat()).willReturn(Boolean.TRUE);
        given(man.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(man).eat(eq(destinationCell));

        // then
        assertThatExceptionOfType(CanNotEatException.class)
                .isThrownBy(() -> man.eat(destinationCell));
    }

    @Test
    void testEat() throws CheckersException {
        // given
        var destinationCell = mock(Cell.class);

        var targetCell = mock(Cell.class);

        var sourceCell = mock(Cell.class);
        given(sourceCell.between(eq(destinationCell), any())).willReturn(targetCell);

        var man = mock(Man.class);
        ReflectionTestUtils.setField(man, "cell", sourceCell);
        given(man.isCanEat()).willReturn(Boolean.TRUE);
        given(man.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(man).eat(eq(destinationCell));

        // when
        man.eat(destinationCell);

        // then
        then(sourceCell)
                .should(times(1))
                .setPiece(isNull());
        then(targetCell)
                .should(times(1))
                .setPiece(isNull());
        then(destinationCell)
                .should(times(1))
                .setPiece(refEq(man));
    }

    @Test
    void testEatWhileWhiteManShouldBecomeKing() throws CheckersException {
        // given
        var destinationCell = mock(Cell.class);
        given(destinationCell.getRow()).willReturn(8);

        var targetCell = mock(Cell.class);

        var sourceCell = mock(Cell.class);
        given(sourceCell.between(eq(destinationCell), any())).willReturn(targetCell);

        var man = mock(Man.class);
        ReflectionTestUtils.setField(man, "cell", sourceCell);
        ReflectionTestUtils.setField(man, "color", Color.WHITE);
        given(man.isCanEat()).willReturn(Boolean.TRUE);
        given(man.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(man).eat(eq(destinationCell));

        // when
        man.eat(destinationCell);

        // then
        then(sourceCell)
                .should(times(1))
                .setPiece(isNull());
        then(targetCell)
                .should(times(1))
                .setPiece(isNull());
        then(destinationCell)
                .should(times(1))
                .setPiece(argThat(piece -> piece instanceof King && piece.getColor() == Color.WHITE));
    }

    @Test
    void testEatWhileBlackManShouldBecomeKing() throws CheckersException {
        // given
        var destinationCell = mock(Cell.class);
        given(destinationCell.getRow()).willReturn(1);

        var targetCell = mock(Cell.class);

        var sourceCell = mock(Cell.class);
        given(sourceCell.between(eq(destinationCell), any())).willReturn(targetCell);

        var man = mock(Man.class);
        ReflectionTestUtils.setField(man, "cell", sourceCell);
        ReflectionTestUtils.setField(man, "color", Color.BLACK);
        given(man.isCanEat()).willReturn(Boolean.TRUE);
        given(man.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(man).eat(eq(destinationCell));

        // when
        man.eat(destinationCell);

        // then
        then(sourceCell)
                .should(times(1))
                .setPiece(isNull());
        then(targetCell)
                .should(times(1))
                .setPiece(isNull());
        then(destinationCell)
                .should(times(1))
                .setPiece(argThat(piece -> piece instanceof King && piece.getColor() == Color.BLACK));
    }
}
