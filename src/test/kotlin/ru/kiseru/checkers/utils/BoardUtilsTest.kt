package ru.kiseru.checkers.utils

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import ru.kiseru.checkers.exception.ConvertCellException

class BoardUtilsTest {

    @ParameterizedTest
    @CsvSource(
        "0, 1, false",
        "1, 1, true",
        "8, 1, true",
        "9, 1, false",
        "1, 0, false",
        "1, 9, false",
    )
    fun `isCoordinatesExists test`(row: Int, column: Int, expected: Boolean) {
        // when
        val actual = isCoordinatesExists(row, column)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "0, false",
        "1, true",
        "8, true",
        "9, false",
    )
    fun `isCoordinateExists test`(coordinate: Int, expected: Boolean) {
        // when
        val actual = isCoordinateExists(coordinate)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(chars = ['`', 'i'])
    fun `convertColumn test when illegal column name was given`(columnName: Char) {
        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { convertColumn(columnName) }
    }

    @ParameterizedTest
    @CsvSource(
        "a, 1",
        "b, 2",
        "c, 3",
        "d, 4",
        "e, 5",
        "f, 6",
        "g, 7",
        "h, 8",
    )
    fun `convertColumn test when legal column name was given`(columnName: Char, expected: Int) {
        // when
        val actual = convertColumn(columnName)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(chars = ['0', '9'])
    fun `convertRow test when illegal row name was given`(rowName: Char) {
        // when & then
        assertThatExceptionOfType(ConvertCellException::class.java)
            .isThrownBy { convertRow(rowName) }
    }

    @ParameterizedTest
    @CsvSource(
        "1, 1",
        "2, 2",
        "3, 3",
        "4, 4",
        "5, 5",
        "6, 6",
        "7, 7",
        "8, 8",
    )
    fun `convertRow test when legal row name was given`(rowName: Char, expected: Int) {
        // when
        val actual = convertRow(rowName)

        // then
        assertThat(actual).isEqualTo(expected)
    }
}
