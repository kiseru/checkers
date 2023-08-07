package ru.kiseru.checkers.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kiseru.checkers.exception.CannotEatException;
import ru.kiseru.checkers.exception.CannotMoveException;
import ru.kiseru.checkers.exception.MustEatException;
import ru.kiseru.checkers.utils.Color;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class KingTest {

    @Mock
    private King underTest;

    @Mock
    private Cell sourceCell;

    @Mock
    private Cell destinationCell;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "cell", sourceCell);
    }

    @Test
    void testIsAbleToMoveToWhileDiffIsMinusOne() {
        // given
        given(sourceCell.diff(eq(destinationCell))).willReturn(-1);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToMoveToWhileDestinationCellIsBusy() {
        // given
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isAbleToMoveToWhileDestinationCellIsOnOtherDiagonal() {
        // given
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(3);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isAbleToMoveToWhileThereIsPieceBetweenSourceCellAndDestinationCell() {
        // given
        var emptyCell = mock(Cell.class);

        var notEmptyCell = mock(Cell.class);
        given(notEmptyCell.getPiece()).willReturn(mock(Piece.class));

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);
        given(board.getCell(eq(4), eq(4))).willReturn(notEmptyCell);

        given(sourceCell.diff(eq(destinationCell))).willReturn(1);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.getRow()).willReturn(6);
        given(destinationCell.getColumn()).willReturn(6);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void isAbleToMoveToWhileThereIsNoPieceBetweenSourceCellAndDestinationCell() {
        // given
        var emptyCell = mock(Cell.class);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);
        given(board.getCell(eq(4), eq(4))).willReturn(emptyCell);
        given(board.getCell(eq(5), eq(5))).willReturn(emptyCell);

        given(sourceCell.diff(eq(destinationCell))).willReturn(1);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.getRow()).willReturn(6);
        given(destinationCell.getColumn()).willReturn(6);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void isAbleToEatToWhileDestinationCellIsNotEmpty() {
        // given
        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("testIsAbleToEatToWhileThereIsLessThanOneCellBetweenSourceAndDestinationSource")
    void testIsAbleToEatToWhileThereIsLessThanOneCellBetweenSourceAndDestination(
            int sourceRow,
            int sourceColumn,
            int destinationRow,
            int destinationColumn
    ) {
        // given
        given(destinationCell.getRow()).willReturn(destinationRow);
        given(destinationCell.getColumn()).willReturn(destinationColumn);

        given(sourceCell.getRow()).willReturn(sourceRow);
        given(sourceCell.getColumn()).willReturn(sourceColumn);
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    private static Arguments[] testIsAbleToEatToWhileThereIsLessThanOneCellBetweenSourceAndDestinationSource() {
        return new Arguments[]{
                Arguments.of(4, 4, 6, 6),
                Arguments.of(4, 4, 1, 7)
        };
    }

    @Test
    void testIsAbleToEatToWhileDestinationCellIsOnOtherDiagonal() {
        // given
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(3);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileThereIsNoPieceBetweenSourceAndDestination() {
        // given
        var emptyCell = mock(Cell.class);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileThereIsPlayerPieceBetweenSourceAndDestination() {
        // given
        var playerPiece = mock(Piece.class);
        given(playerPiece.getColor()).willReturn(Color.WHITE);

        var cellWithPlayerPiece = mock(Cell.class);
        given(cellWithPlayerPiece.getPiece()).willReturn(playerPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithPlayerPiece);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.getColor()).willReturn(Color.WHITE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileThereAreTwoEnemyPiecesBetweenSourceAndDestination() {
        // given
        var enemyPiece = mock(Piece.class);
        given(enemyPiece.getColor()).willReturn(Color.BLACK);

        var cellWithEnemyPiece = mock(Cell.class);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece);
        given(board.getCell(eq(4), eq(4))).willReturn(cellWithEnemyPiece);

        given(destinationCell.getRow()).willReturn(5);
        given(destinationCell.getColumn()).willReturn(5);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.getColor()).willReturn(Color.WHITE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testIsAbleToEatToWhileThereIsOneEnemyPieceBetweenSourceAndDestination() {
        // given
        var enemyPiece = mock(Piece.class);
        given(enemyPiece.getColor()).willReturn(Color.BLACK);

        var cellWithEnemyPiece = mock(Cell.class);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.getColor()).willReturn(Color.WHITE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToEatTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testIsKing() {
        // given
        given(underTest.isKing()).willCallRealMethod();

        // when
        var actual = underTest.isKing();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void testIsMan() {
        // given
        given(underTest.isMan()).willCallRealMethod();

        // when
        var actual = underTest.isMan();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void testMoveWhileCanEat() {
        // given
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(MustEatException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMove() {
        // given
        ReflectionTestUtils.setField(underTest, "isCanEat", Boolean.FALSE);
        ReflectionTestUtils.setField(underTest, "isCanMove", Boolean.FALSE);
        given(underTest.getCell()).willReturn(sourceCell);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMoveToDestination() {
        // given
        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCanMoveToDestination() {
        // given
        ReflectionTestUtils.setField(sourceCell, "piece", underTest);
        willCallRealMethod().given(sourceCell).setPiece(any());

        ReflectionTestUtils.setField(destinationCell, "piece", null);
        willCallRealMethod().given(destinationCell).setPiece(any());

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isCanEat()).willReturn(Boolean.FALSE);
        given(underTest.isCanMove()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // when
        underTest.move(destinationCell);

        // then
        var sourceCellPiece = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(sourceCellPiece).isNull();

        var destinationCellPiece = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(destinationCellPiece).isSameAs(underTest);
    }

    @Test
    void testEatWhileCannotEat() {
        // given
        ReflectionTestUtils.setField(underTest, "isCanEat", Boolean.FALSE);
        given(underTest.getCell()).willReturn(sourceCell);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileCannotEatToDestination() {
        // given
        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // when && then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileThereIsNoSacrificedPiece() {
        // given
        var emptyCell = mock(Cell.class);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileThereIsPlayerPiece() {
        // given
        var playerPiece = mock(Piece.class);
        given(playerPiece.getColor()).willReturn(Color.WHITE);

        var cellWithPlayerPiece = mock(Cell.class);
        given(cellWithPlayerPiece.getPiece()).willReturn(playerPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithPlayerPiece);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.getColor()).willReturn(Color.WHITE);
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileThereIsSacrificedPiece() {
        // given
        var enemyPiece = mock(Piece.class);
        given(enemyPiece.getColor()).willReturn(Color.BLACK);

        var cellWithEnemyPiece = mock(Cell.class);
        ReflectionTestUtils.setField(cellWithEnemyPiece, "piece", enemyPiece);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);
        willCallRealMethod().given(cellWithEnemyPiece).setPiece(any());

        given(enemyPiece.getCell()).willReturn(cellWithEnemyPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece);

        ReflectionTestUtils.setField(sourceCell, "piece", underTest);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);
        willCallRealMethod().given(sourceCell).setPiece(any());

        ReflectionTestUtils.setField(destinationCell, "piece", null);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);
        willCallRealMethod().given(destinationCell).setPiece(any());

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.getColor()).willReturn(Color.WHITE);
        given(underTest.isCanEat()).willReturn(Boolean.TRUE);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // when
        underTest.eat(destinationCell);

        // then
        var sourceCellPiece = ReflectionTestUtils.getField(sourceCell, "piece");
        assertThat(sourceCellPiece).isNull();

        var sacrificedPiece = ReflectionTestUtils.getField(cellWithEnemyPiece, "piece");
        assertThat(sacrificedPiece).isNull();

        var destinationCellPiece = ReflectionTestUtils.getField(destinationCell, "piece");
        assertThat(destinationCellPiece).isSameAs(underTest);
    }

    @ParameterizedTest
    @MethodSource("testToStringSource")
    void testToString(Color color, String expected) {
        // given
        given(underTest.getColor()).willReturn(color);
        given(underTest.toString()).willCallRealMethod();

        // when
        var actual = underTest.toString();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Arguments[] testToStringSource() {
        return new Arguments[] {
                Arguments.of(Color.WHITE, "#"),
                Arguments.of(Color.BLACK, "&")
        };
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileThereIsNoMoves() {
        // given
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getNear(eq(1), eq(1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(1))).willReturn(destinationCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanMove");
        assertThat(actual).isEqualTo(Boolean.FALSE);
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileCanMoveByFirstDirection() {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getNear(eq(1), eq(1))).willReturn(targetCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanMove");
        assertThat(actual).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileCanMoveBySecondDirection() {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getNear(eq(1), eq(1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(1), eq(-1))).willReturn(targetCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanMove");
        assertThat(actual).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileCanMoveByThirdDirection() {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getNear(eq(1), eq(1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(-1))).willReturn(targetCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanMove");
        assertThat(actual).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileCanMoveByForthDirection() {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getNear(eq(1), eq(1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(1))).willReturn(targetCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanMove");
        assertThat(actual).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testAnalyzeAbilityOfEatWhileThereIsNoMoves() {
        // given
        given(sourceCell.getRow()).willReturn(4);
        given(sourceCell.getColumn()).willReturn(4);
        given(sourceCell.getNear(anyInt(), anyInt())).willReturn(destinationCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        willCallRealMethod().given(underTest).analyzeAbilityOfEat();

        // when
        underTest.analyzeAbilityOfEat();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanEat");
        assertThat(actual).isEqualTo(Boolean.FALSE);
    }

    @MockitoSettings(strictness = Strictness.WARN)
    @ParameterizedTest
    @MethodSource("testAnalyzeAbilityOfEatWhileCanMoveByOneDirectionSource")
    void testAnalyzeAbilityOfEatWhileCanMoveByOneDirection(int diffRow, int diffColumn) {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.getRow()).willReturn(4);
        given(sourceCell.getColumn()).willReturn(4);
        given(sourceCell.getNear(anyInt(), anyInt())).willReturn(destinationCell);
        given(sourceCell.getNear(eq(diffRow), eq(diffColumn))).willReturn(targetCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToEatTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToEatTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanEat(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfEat();

        // when
        underTest.analyzeAbilityOfEat();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "isCanEat");
        assertThat(actual).isEqualTo(Boolean.TRUE);
    }

    private static Arguments[] testAnalyzeAbilityOfEatWhileCanMoveByOneDirectionSource() {
        return new Arguments[] {
                Arguments.of(2, 2),
                Arguments.of(2, -2),
                Arguments.of(-2, -2),
                Arguments.of(-2, 2)
        };
    }
}