package com.checkers.board;

import com.checkers.exceptions.CannotEatException;
import com.checkers.exceptions.CannotMoveException;
import com.checkers.exceptions.MustEatException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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

        given(destinationCell.isEmpty()).willReturn(Boolean.FALSE);

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

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(3);

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
        given(emptyCell.isEmpty()).willReturn(Boolean.TRUE);

        var notEmptyCell = mock(Cell.class);
        given(notEmptyCell.isEmpty()).willReturn(Boolean.FALSE);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);
        given(board.getCell(eq(4), eq(4))).willReturn(notEmptyCell);

        given(sourceCell.diff(eq(destinationCell))).willReturn(1);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(6);
        given(destinationCell.getColumn()).willReturn(6);

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
        given(emptyCell.isEmpty()).willReturn(Boolean.TRUE);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);
        given(board.getCell(eq(4), eq(4))).willReturn(emptyCell);
        given(board.getCell(eq(5), eq(5))).willReturn(emptyCell);

        given(sourceCell.diff(eq(destinationCell))).willReturn(1);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(6);
        given(destinationCell.getColumn()).willReturn(6);

        given(underTest.isAbleToMoveTo(eq(destinationCell))).willCallRealMethod();

        // when
        var actual = underTest.isAbleToMoveTo(destinationCell);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void isAbleToEatToWhileDestinationCellIsNotEmpty() {
        // given
        given(destinationCell.isEmpty()).willReturn(Boolean.FALSE);

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
        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(destinationRow);
        given(destinationCell.getColumn()).willReturn(destinationColumn);

        given(sourceCell.getRow()).willReturn(sourceRow);
        given(sourceCell.getColumn()).willReturn(sourceColumn);
        given(sourceCell.diff(eq(destinationCell))).willReturn(1);

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
        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(3);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);

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
        given(emptyCell.isEmpty()).willReturn(Boolean.TRUE);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

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
        given(cellWithPlayerPiece.isEmpty()).willReturn(Boolean.FALSE);
        given(cellWithPlayerPiece.getPiece()).willReturn(playerPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithPlayerPiece);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
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
        given(cellWithEnemyPiece.isEmpty()).willReturn(Boolean.FALSE);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece);
        given(board.getCell(eq(4), eq(4))).willReturn(cellWithEnemyPiece);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(5);
        given(destinationCell.getColumn()).willReturn(5);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
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
        given(cellWithEnemyPiece.isEmpty()).willReturn(Boolean.FALSE);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece);

        given(destinationCell.isEmpty()).willReturn(Boolean.TRUE);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.diff(eq(destinationCell))).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
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
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.TRUE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(MustEatException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMove() {
        // given
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.FALSE);
        ReflectionTestUtils.setField(underTest, "canMove", Boolean.FALSE);
        willCallRealMethod().given(underTest).move(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotMoveException.class)
                .isThrownBy(() -> underTest.move(destinationCell));
    }

    @Test
    void testMoveWhileCannotMoveToDestination() {
        // given
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.FALSE);
        ReflectionTestUtils.setField(underTest, "canMove", Boolean.TRUE);
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
        willCallRealMethod().given(sourceCell).setPiece(isNull());

        ReflectionTestUtils.setField(destinationCell, "piece", null);
        willCallRealMethod().given(destinationCell).setPiece(eq(underTest));

        ReflectionTestUtils.setField(underTest, "canEat", Boolean.FALSE);
        ReflectionTestUtils.setField(underTest, "canMove", Boolean.TRUE);
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
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.FALSE);
        willCallRealMethod().given(underTest).eat(eq(destinationCell));

        // when & then
        assertThatExceptionOfType(CannotEatException.class)
                .isThrownBy(() -> underTest.eat(destinationCell));
    }

    @Test
    void testEatWhileCannotEatToDestination() {
        // given
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.TRUE);
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
        given(emptyCell.isEmpty()).willReturn(Boolean.TRUE);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(emptyCell);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        ReflectionTestUtils.setField(underTest, "canEat", Boolean.TRUE);
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
        given(cellWithPlayerPiece.isEmpty()).willReturn(Boolean.FALSE);
        given(cellWithPlayerPiece.getPiece()).willReturn(playerPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithPlayerPiece);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);

        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.TRUE);
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
        given(cellWithEnemyPiece.isEmpty()).willReturn(Boolean.FALSE);
        given(cellWithEnemyPiece.getPiece()).willReturn(enemyPiece);
        willCallRealMethod().given(cellWithEnemyPiece).setPiece(isNull());

        given(enemyPiece.getCell()).willReturn(cellWithEnemyPiece);

        var board = mock(Board.class);
        given(board.getCell(eq(3), eq(3))).willReturn(cellWithEnemyPiece);

        ReflectionTestUtils.setField(sourceCell, "piece", underTest);
        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getBoard()).willReturn(board);
        willCallRealMethod().given(sourceCell).setPiece(isNull());

        ReflectionTestUtils.setField(destinationCell, "piece", null);
        given(destinationCell.getRow()).willReturn(4);
        given(destinationCell.getColumn()).willReturn(4);
        willCallRealMethod().given(destinationCell).setPiece(eq(underTest));

        ReflectionTestUtils.setField(underTest, "color", Color.WHITE);
        ReflectionTestUtils.setField(underTest, "canEat", Boolean.TRUE);
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
        ReflectionTestUtils.setField(underTest, "color", color);
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
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "canMove");
        assertThat(actual).isEqualTo(Boolean.FALSE);
    }

    @Test
    void testAnalyzeAbilityOfMoveWhileCanMoveByFirstDirection() {
        // given
        var targetCell = mock(Cell.class);

        given(sourceCell.getRow()).willReturn(2);
        given(sourceCell.getColumn()).willReturn(2);
        given(sourceCell.getNear(eq(1), eq(1))).willReturn(targetCell);
        given(sourceCell.getNear(eq(1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(1))).willReturn(destinationCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "canMove");
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
        given(sourceCell.getNear(eq(-1), eq(-1))).willReturn(destinationCell);
        given(sourceCell.getNear(eq(-1), eq(1))).willReturn(destinationCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "canMove");
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
        given(sourceCell.getNear(eq(-1), eq(1))).willReturn(destinationCell);

        given(underTest.getCell()).willReturn(sourceCell);
        given(underTest.isAbleToMoveTo(eq(destinationCell))).willReturn(Boolean.FALSE);
        given(underTest.isAbleToMoveTo(eq(targetCell))).willReturn(Boolean.TRUE);
        willCallRealMethod().given(underTest).setCanMove(anyBoolean());
        willCallRealMethod().given(underTest).analyzeAbilityOfMove();

        // when
        underTest.analyzeAbilityOfMove();

        // then
        var actual = ReflectionTestUtils.getField(underTest, "canMove");
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
        var actual = ReflectionTestUtils.getField(underTest, "canMove");
        assertThat(actual).isEqualTo(Boolean.TRUE);
    }

}
