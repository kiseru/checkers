package com.checkers.utils;

import com.checkers.exceptions.ConvertCellException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    @ParameterizedTest
    @ValueSource(chars = { '`', 'i' })
    void testConvertColumnWhileIllegalColumnName(char columnName) {
        // when & then
        assertThatExceptionOfType(ConvertCellException.class)
                .isThrownBy(() -> BoardUtils.convertColumn(columnName));
    }

    @ParameterizedTest
    @MethodSource("testConvertColumnWhileLegalColumnNameSource")
    void testConvertColumnWhileLegalColumnName(char columnName, int expected) {
        // when
        var actual = BoardUtils.convertColumn(columnName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Arguments[] testConvertColumnWhileLegalColumnNameSource() {
        return new Arguments[] {
                Arguments.of('a', 1),
                Arguments.of('b', 2),
                Arguments.of('c', 3),
                Arguments.of('d', 4),
                Arguments.of('e', 5),
                Arguments.of('f', 6),
                Arguments.of('g', 7),
                Arguments.of('h', 8)
        };
    }

    @ParameterizedTest
    @ValueSource(chars = { '0', '9' })
    void testConvertRowWhileIllegalRowName(char rowName) {
        // when & then
        assertThatExceptionOfType(ConvertCellException.class)
                .isThrownBy(() -> BoardUtils.convertRow(rowName));
    }

    @ParameterizedTest
    @MethodSource("testConvertTowWhileLegalRowNameSource")
    void testConvertTowWhileLegalRowName(char rowName, int expected) {
        // when
        var actual = BoardUtils.convertRow(rowName);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private static Arguments[] testConvertTowWhileLegalRowNameSource() {
        return new Arguments[] {
                Arguments.of('1', 1),
                Arguments.of('2', 2),
                Arguments.of('3', 3),
                Arguments.of('4', 4),
                Arguments.of('5', 5),
                Arguments.of('6', 6),
                Arguments.of('7', 7),
                Arguments.of('8', 8),
        };
    }
}