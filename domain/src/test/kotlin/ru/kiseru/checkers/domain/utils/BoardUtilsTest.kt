package ru.kiseru.checkers.domain.utils

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ru.kiseru.checkers.domain.exception.CellNotFoundException

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

    @Test
    fun `test getCellCaption when coordinates exist`() {
        // when
        val actual = getCellCaption(1 to 1)

        // then
        assertThat(actual).isEqualTo("a1")
    }

    @Test
    fun `test getCellCaption when coordinates don't exist`() {
        // when & then
        assertThatExceptionOfType(CellNotFoundException::class.java)
            .isThrownBy { getCellCaption(0 to 0) }
    }
}
