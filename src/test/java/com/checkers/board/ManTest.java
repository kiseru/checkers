package com.checkers.board;

import com.checkers.exceptions.CanEatException;
import com.checkers.exceptions.CanNotEatException;
import com.checkers.exceptions.CanNotMoveException;
import com.checkers.exceptions.CheckersException;
import com.checkers.utils.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

    private static final int SOURCE_CELL_COLUMN = 4;

    private static final int SOURCE_CELL_ROW = 4;

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

        given(board.getCell(eq(SOURCE_CELL_ROW + 1), eq(SOURCE_CELL_COLUMN - 1))).willReturn(destinationCell);
        given(board.getCell(eq(SOURCE_CELL_ROW + 1), eq(SOURCE_CELL_COLUMN + 1))).willReturn(destinationCell);
        given(board.getCell(eq(SOURCE_CELL_ROW - 1), eq(SOURCE_CELL_COLUMN - 1))).willReturn(emptyCell);
        given(board.getCell(eq(SOURCE_CELL_ROW - 1), eq(SOURCE_CELL_COLUMN + 1))).willReturn(emptyCell);

        given(sourceCell.getCol()).willReturn(SOURCE_CELL_COLUMN);
        given(sourceCell.getRow()).willReturn(SOURCE_CELL_ROW);
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testIsAbleToEatToWhileCellDiffIsLessThanTwo() {
        // given
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);

        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileCellDiffIsMoreThanTwo() {
        // given
        given(sourceCell.diff(eq(destinationCell))).willReturn(3);

        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileDestinationCellIsNotBusy() {
        // given
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);

        given(destinationCell.isEmpty()).willReturn(Boolean.FALSE);

        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileThereIsNoSacrificePiece() {
        // given
        var cellWithSacrifice = mock(Cell.class);
        given(cellWithSacrifice.getPiece()).willReturn(null);

        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.between(eq(destinationCell), eq(board))).willReturn(cellWithSacrifice);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileSacrificePieceHasTheSameColor() {
        // given
        var sacrificePiece = mock(Man.class);
        ReflectionTestUtils.setField(sacrificePiece, "color", Color.WHITE);

        var cellWithSacrifice = mock(Cell.class);
        given(cellWithSacrifice.getPiece()).willReturn(sacrificePiece);

        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.between(eq(destinationCell), eq(board))).willReturn(cellWithSacrifice);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileSacrificePieceHasEnemyColor() {
        // given
        var sacrificePiece = mock(Man.class);
        ReflectionTestUtils.setField(sacrificePiece, "color", Color.BLACK);

        var cellWithSacrifice = mock(Cell.class);
        given(cellWithSacrifice.getPiece()).willReturn(sacrificePiece);

        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.between(eq(destinationCell), eq(board))).willReturn(cellWithSacrifice);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testAnalyzeAbilityOfMove() {
        // given
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.getCol()).willReturn(SOURCE_CELL_COLUMN);
        given(sourceCell.getRow()).willReturn(SOURCE_CELL_ROW);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
        given(underTest.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var canMove = ReflectionTestUtils.getField(underTest, "canMove");
        assertThat(canMove).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testAnalyzeAbilityOfEat() {
        // given
        given(sourceCell.getBoard()).willReturn(board);
        given(sourceCell.getCol()).willReturn(SOURCE_CELL_COLUMN);
        given(sourceCell.getRow()).willReturn(SOURCE_CELL_ROW);

        given(underTest.isAbleToEatTo(any())).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).analyzeAbilityOfEat();

        // when
        underTest.analyzeAbilityOfEat();

        // then
        var canEat = ReflectionTestUtils.getField(underTest, "canEat");
        assertThat(canEat).isEqualTo(Boolean.TRUE);
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
