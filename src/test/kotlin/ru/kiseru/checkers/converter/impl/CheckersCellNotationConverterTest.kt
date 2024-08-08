package ru.kiseru.checkers.converter.impl

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class CheckersCellNotationConverterTest {

    private val underTest = CheckersCellNotationConverter()

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa", "`2", "i8", "a0", "a9"])
    fun `makeTurn test when source cell isn't valid`(turnNotation: String) {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert(turnNotation) }
    }

    @ParameterizedTest
    @CsvSource("a1,1,1", "h8,8,8", "a8,8,1", "h1,1,8")
    fun `makeTurn test when source cell is valid`(turnNotation: String, row: Int, column: Int) {
        // when
        val actual = underTest.convert(turnNotation)

        // then
        assertThat(actual).isEqualTo(row to column)
    }
}
