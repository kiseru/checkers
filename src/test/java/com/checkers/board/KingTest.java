package com.checkers.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
}