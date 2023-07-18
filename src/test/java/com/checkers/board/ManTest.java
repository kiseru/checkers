package com.checkers.board;

import com.checkers.exceptions.MustEatException;
import com.checkers.exceptions.CannotMoveException;
import com.checkers.exceptions.CannotEatException;
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
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.willCallRealMethod;

@ExtendWith(MockitoExtension.class)
class ManTest {

    private static final int SOURCE_CELL_COLUMN = 4;

    private static final int SOURCE_CELL_ROW = 4;

    @Mock
    private Man underTest;

    @Mock
    private Board board;

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
    void testMoveWhileCanEat() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // then
        assertThatExceptionOfType(MustEatException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMove() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMoveTo() {
        // given
        var underTest = mock(Man.class);
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMove() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);

        willCallRealMethod().given(underTest).move(destinationCell);
        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(sourceCell).setPiece(any());

        // when
        underTest.move(destinationCell);

        // then
        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isEqualTo(underTest);

        var pieceInSourceCell = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(pieceInSourceCell).isNull();
    }

    @Test
    void testMoveWhileWhitePieceBecomeKing() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);

        given(destinationCell.getRow()).willReturn(8);

        willCallRealMethod().given(underTest).move(destinationCell);
        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(sourceCell).setPiece(any());

        // when
        underTest.move(destinationCell);

        // then
        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isNotNull();
        assertThat(pieceInDestinationCell).isInstanceOf(King.class);
        assertThat(((King) pieceInDestinationCell).getColor()).isEqualTo(Color.WHITE);

        var pieceInSourceCell = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(pieceInSourceCell).isNull();
    }

    @Test
    void testMoveWhileBlackPieceBecomeKing() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToMoveTo(any())).willReturn(Boolean.TRUE);

        ReflectionTestUtils.setField(underTest, "color", Color.BLACK);

        given(destinationCell.getRow()).willReturn(1);

        willCallRealMethod().given(underTest).move(destinationCell);
        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(sourceCell).setPiece(any());

        // when
        underTest.move(destinationCell);

        // then
        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isNotNull();
        assertThat(pieceInDestinationCell).isInstanceOf(King.class);
        assertThat(((King) pieceInDestinationCell).getColor()).isEqualTo(Color.BLACK);

        var pieceInSourceCell = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(pieceInSourceCell).isNull();
    }

    @Test
    void testEatWhileCannotEat() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileCannotEatAtSomeDestination() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEat() {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.between(eq(destinationCell), any())).willReturn(targetCell);

        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));
        willCallRealMethod().given(sourceCell).setPiece(any());
        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(targetCell).setPiece(any());

        // when
        underTest.eat(destinationCell);

        // then
        var pieceInSourceCell = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(pieceInSourceCell).isNull();

        var pieceInTargetCell = ReflectionTestUtils.getField(targetCell, "piece");
        assertThat(pieceInTargetCell).isNull();

        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isEqualTo(underTest);
    }

    @Test
    void testEatWhileWhiteManShouldBecomeKing() {
        // given
        given(destinationCell.getRow()).willReturn(8);

        var targetCell = mock(Cell.class);

        given(sourceCell.between(eq(destinationCell), any())).willReturn(targetCell);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));
        willCallRealMethod().given(sourceCell).setPiece(any());
        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(targetCell).setPiece(any());

        // when
        underTest.eat(destinationCell);

        // then
        var pieceInSourceCell = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(pieceInSourceCell).isNull();

        var pieceInTargetCell = ReflectionTestUtils.getField(targetCell, "piece");
        assertThat(pieceInTargetCell).isNull();

        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isNotNull();
        assertThat(pieceInDestinationCell).isInstanceOf(King.class);
        assertThat(((King) pieceInDestinationCell).getColor()).isEqualTo(Color.WHITE);
    }

    @Test
    void testEatWhileBlackManShouldBecomeKing() {
        // given
        given(destinationCell.getRow()).willReturn(1);

        var targetCell = mock(Cell.class);

        given(sourceCell.between(eq(destinationCell), any())).willReturn(targetCell);

        ReflectionTestUtils.setField(underTest, "color", Color.BLACK);
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));
        willCallRealMethod().given(sourceCell).setPiece(any());
        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(targetCell).setPiece(any());

        // when
        underTest.eat(destinationCell);

        // then
        var pieceInSourceCell = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(pieceInSourceCell).isNull();

        var pieceInTargetCell = ReflectionTestUtils.getField(targetCell, "piece");
        assertThat(pieceInTargetCell).isNull();

        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isNotNull();
        assertThat(pieceInDestinationCell).isInstanceOf(King.class);
        assertThat(((King) pieceInDestinationCell).getColor()).isEqualTo(Color.BLACK);
    }

    @Test
    void testCreating() {
        // when
        var man = new Man(Color.BLACK);

        // then
        assertThat(man.getColor()).isEqualTo(Color.BLACK);
    }
}
