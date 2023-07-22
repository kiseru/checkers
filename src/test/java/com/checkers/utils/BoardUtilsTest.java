package com.checkers.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

class BoardUtilsTest {

    @ParameterizedTest
    @MethodSource("testIsCoordinateExistsSource")
    void testIsCoordinateExists(int coordinate, boolean expected) {
        // when
        var actual = BoardUtils.isCoordinateExists(coordinate);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Arguments[] testIsCoordinateExistsSource() {
        return new Arguments[] {
                Arguments.of(0, Boolean.FALSE),
                Arguments.of(1, Boolean.TRUE),
                Arguments.of(8, Boolean.TRUE),
                Arguments.of(9, Boolean.FALSE)
        };
    }
}