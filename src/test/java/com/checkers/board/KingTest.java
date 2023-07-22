package com.checkers.board;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
}
