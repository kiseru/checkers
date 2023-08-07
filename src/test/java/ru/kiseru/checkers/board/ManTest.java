package ru.kiseru.checkers.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kiseru.checkers.exception.CannotEatException;
import ru.kiseru.checkers.exception.CannotMoveException;
import ru.kiseru.checkers.exception.MustEatException;
import ru.kiseru.checkers.utils.Color;

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

    @InjectMocks
    private Man underTest = new Man(Color.WHITE);

    @Mock
    private Board board;

    @Mock
    private Cell cell;

    @Mock
    private Cell destinationCell;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cell, "piece", underTest);
    }

    @Test
    void testIsAbleToMoveToWhileDestinationCellIsNotEmpty() {
        // given
        given(destinationCell.getPiece()).willReturn(mock(Piece.class));

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileEnemyIsNearby() {
        // given
        var enemy = mock(Man.class);
        ReflectionTestUtils.setField(enemy, "color", Color.BLACK);

        var cellWithEnemy = mock(Cell.class);
        given(cellWithEnemy.getPiece()).willReturn(enemy);

        given(board.getCell(anyInt(), anyInt())).willReturn(cellWithEnemy);

        given(cell.getBoard()).willReturn(board);

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileDestinationDiffIsMoreThanOne() {
        // given
        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);

        given(cell.getBoard()).willReturn(board);
        given(cell.diff(eq(destinationCell))).willReturn(2);

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileThereIsNoNextRow() {
        // given
        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);

        given(cell.getBoard()).willReturn(board);
        given(cell.diff(eq(destinationCell))).willReturn(1);
        given(cell.getRow()).willReturn(8);

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileCanMoveToDestination() {
        // given
        var emptyCell = mock(Cell.class);
        given(emptyCell.getPiece()).willReturn(null);

        given(board.getCell(eq(SOURCE_CELL_ROW + 1), eq(SOURCE_CELL_COLUMN - 1))).willReturn(destinationCell);
        given(board.getCell(eq(SOURCE_CELL_ROW + 1), eq(SOURCE_CELL_COLUMN + 1))).willReturn(destinationCell);
        given(board.getCell(eq(SOURCE_CELL_ROW - 1), eq(SOURCE_CELL_COLUMN - 1))).willReturn(emptyCell);
        given(board.getCell(eq(SOURCE_CELL_ROW - 1), eq(SOURCE_CELL_COLUMN + 1))).willReturn(emptyCell);

        given(cell.getColumn()).willReturn(SOURCE_CELL_COLUMN);
        given(cell.getRow()).willReturn(SOURCE_CELL_ROW);
        given(cell.getBoard()).willReturn(board);
        given(cell.diff(eq(destinationCell))).willReturn(1);

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testIsAbleToEatToWhileCellDiffIsLessThanTwo() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(1);

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileCellDiffIsMoreThanTwo() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(3);

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileDestinationCellIsNotBusy() {
        // given
        given(cell.diff(eq(destinationCell))).willReturn(2);

        given(destinationCell.getPiece()).willReturn(mock(Piece.class));

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

        given(cell.diff(eq(destinationCell))).willReturn(2);
        given(cell.getBoard()).willReturn(board);
        given(cell.between(eq(destinationCell), eq(board))).willReturn(cellWithSacrifice);

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileSacrificePieceHasTheSameColor() {
        // given
        var sacrificePiece = mock(Man.class);
        given(sacrificePiece.getColor()).willReturn(Color.WHITE);

        var cellWithSacrifice = mock(Cell.class);
        given(cellWithSacrifice.getPiece()).willReturn(sacrificePiece);

        given(cell.diff(eq(destinationCell))).willReturn(2);
        given(cell.getBoard()).willReturn(board);
        given(cell.between(eq(destinationCell), eq(board))).willReturn(cellWithSacrifice);

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

        given(cell.diff(eq(destinationCell))).willReturn(2);
        given(cell.getBoard()).willReturn(board);
        given(cell.between(eq(destinationCell), eq(board))).willReturn(cellWithSacrifice);

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testAnalyzeAbilityOfMove() {
        // given
        var emptyCell = mock(Cell.class);

        given(cell.getBoard()).willReturn(board);
        given(cell.getColumn()).willReturn(SOURCE_CELL_COLUMN);
        given(cell.getRow()).willReturn(SOURCE_CELL_ROW);
        given(cell.diff(eq(emptyCell))).willReturn(1);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var canMove = underTest.isCanMove();
        assertThat(canMove).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testAnalyzeAbilityOfEat() {
        // given
        var emptyCell = mock(Cell.class);

        var enemyPiece = mock(Piece.class);
        ReflectionTestUtils.setField(enemyPiece, "color", Color.BLACK);

        var cellWithEnemyPiece = mock(Cell.class);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);

        given(cell.getBoard()).willReturn(board);
        given(cell.getColumn()).willReturn(SOURCE_CELL_COLUMN);
        given(cell.getRow()).willReturn(SOURCE_CELL_ROW);
        given(cell.diff(eq(emptyCell))).willReturn(2);
        given(cell.between(eq(emptyCell), eq(board))).willReturn(cellWithEnemyPiece);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);

        // when
        underTest.analyzeAbilityOfEat();

        // then
        var canEat = underTest.isCanEat();
        assertThat(canEat).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testMoveWhileCanEat() {
        // given
        underTest.setCanEat(true);

        // then
        assertThatExceptionOfType(MustEatException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMove() {
        // given
        underTest.setCanEat(false);
        underTest.setCanMove(false);

        // then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMoveTo() {
        // given
        underTest.setCanEat(false);
        underTest.setCanMove(true);

        given(destinationCell.getPiece()).willReturn(mock(Piece.class));

        // then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMove() {
        // given
        underTest.setCanEat(false);
        underTest.setCanMove(true);

        var emptyCell = mock(Cell.class);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);
        given(board.getCell(eq(1), eq(1))).willReturn(destinationCell);

        willCallRealMethod().given(cell).setPiece(any());
        given(cell.getBoard()).willReturn(board);
        given(cell.diff(eq(destinationCell))).willReturn(1);


        willCallRealMethod().given(destinationCell).setPiece(any());

        // when
        underTest.move(destinationCell);

        // then
        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isEqualTo(underTest);

        var pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece");
        assertThat(pieceInSourceCell).isNull();
    }

    @Test
    void testMoveWhileWhitePieceBecomeKing() {
        // given
        underTest.setCanEat(false);
        underTest.setCanMove(true);

        var emptyCell = mock(Cell.class);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);
        given(board.getCell(eq(8), eq(2))).willReturn(destinationCell);

        given(cell.getBoard()).willReturn(board);
        given(cell.diff(eq(destinationCell))).willReturn(1);
        given(cell.getRow()).willReturn(7);
        given(cell.getColumn()).willReturn(1);

        given(destinationCell.getRow()).willReturn(8);

        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(cell).setPiece(any());

        // when
        underTest.move(destinationCell);

        // then
        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isNotNull();
        assertThat(pieceInDestinationCell).isInstanceOf(King.class);
        assertThat(((King) pieceInDestinationCell).getColor()).isEqualTo(Color.WHITE);

        var pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece");
        assertThat(pieceInSourceCell).isNull();
    }

    @Test
    void testMoveWhileBlackPieceBecomeKing() {
        // given
        underTest.setCanEat(false);
        underTest.setCanMove(true);

        var emptyCell = mock(Cell.class);

        given(board.getCell(anyInt(), anyInt())).willReturn(emptyCell);
        given(board.getCell(eq(1), eq(1))).willReturn(destinationCell);

        given(cell.getBoard()).willReturn(board);
        given(cell.diff(eq(destinationCell))).willReturn(1);
        given(cell.getRow()).willReturn(2);
        given(cell.getColumn()).willReturn(2);

        given(destinationCell.getRow()).willReturn(1);

        willCallRealMethod().given(destinationCell).setPiece(any());
        willCallRealMethod().given(cell).setPiece(any());

        ReflectionTestUtils.setField(underTest, "color", Color.BLACK);

        // when
        underTest.move(destinationCell);

        // then
        var pieceInDestinationCell = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(pieceInDestinationCell).isNotNull();
        assertThat(pieceInDestinationCell).isInstanceOf(King.class);
        assertThat(((King) pieceInDestinationCell).getColor()).isEqualTo(Color.BLACK);

        var pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece");
        assertThat(pieceInSourceCell).isNull();
    }

    @Test
    void testEatWhileCannotEat() {
        // given
        underTest.setCanEat(false);

        // then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileCannotEatAtSomeDestination() {
        // given
        underTest.setCanEat(true);

        // then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEat() {
        // given
        var enemy = mock(Piece.class);
        ReflectionTestUtils.setField(enemy, "color", Color.BLACK);

        var targetCell = mock(Cell.class);
        given(targetCell.getPiece()).willReturn(enemy);
        willCallRealMethod().given(targetCell).setPiece(any());

        given(cell.between(eq(destinationCell), any())).willReturn(targetCell);
        given(cell.diff(eq(destinationCell))).willReturn(2);
        willCallRealMethod().given(cell).setPiece(any());

        underTest.setCanEat(true);

        willCallRealMethod().given(destinationCell).setPiece(any());

        // when
        underTest.eat(destinationCell);

        // then
        var pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece");
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
        willCallRealMethod().given(destinationCell).setPiece(any());

        var enemy = mock(Piece.class);
        ReflectionTestUtils.setField(enemy, "color", Color.BLACK);

        var targetCell = mock(Cell.class);
        given(targetCell.getPiece()).willReturn(enemy);
        willCallRealMethod().given(targetCell).setPiece(any());

        given(cell.between(eq(destinationCell), any())).willReturn(targetCell);
        given(cell.diff(eq(destinationCell))).willReturn(2);
        willCallRealMethod().given(cell).setPiece(any());

        underTest.setCanEat(true);

        // when
        underTest.eat(destinationCell);

        // then
        var pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece");
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
        willCallRealMethod().given(destinationCell).setPiece(any());

        var enemy = mock(Piece.class);
        ReflectionTestUtils.setField(enemy, "color", Color.WHITE);

        var targetCell = mock(Cell.class);
        given(targetCell.getPiece()).willReturn(enemy);
        willCallRealMethod().given(targetCell).setPiece(any());

        given(cell.between(eq(destinationCell), any())).willReturn(targetCell);
        given(cell.diff(eq(destinationCell))).willReturn(2);
        willCallRealMethod().given(cell).setPiece(any());

        ReflectionTestUtils.setField(underTest, "color", Color.BLACK);
        underTest.setCanEat(true);

        // when
        underTest.eat(destinationCell);

        // then
        var pieceInSourceCell = ReflectionTestUtils.getField(cell, "piece");
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

    @Test
    void testIsMan() {
        // when
        var actual = underTest.isMan();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testIsKing() {
        // when
        var actual = underTest.isKing();

        // then
        assertThat(actual).isFalse();
    }
}
