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
}