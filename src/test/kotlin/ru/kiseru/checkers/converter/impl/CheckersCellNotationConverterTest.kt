package ru.kiseru.checkers.converter.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class CheckersCellNotationConverterTest {

    private val underTest = CheckersCellNotationConverter()

    @ParameterizedTest
    @ValueSource(strings = ["a", "aaa"])
    fun `makeTurn test when source cell isn't valid`(turnNotation: String) {
        // when
        val actual = underTest.convert(turnNotation)

        // then
        assertThat(actual.isLeft()).isTrue
        actual.onLeft { assertThat(it).isEqualTo("Can't convert '$turnNotation' to cell") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["`2", "i8"])
    fun `makeTurn test when source cell column isn't valid`(turnNotation: String) {
        // when
        val actual = underTest.convert(turnNotation)

        // then
        assertThat(actual.isLeft()).isTrue
        actual.onLeft { assertThat(it).isEqualTo("Column '${turnNotation[0]}' doesn't exists") }
    }

    @ParameterizedTest
    @ValueSource(strings = ["a0", "a9"])
    fun `makeTurn test when source cell row isn't valid`(turnNotation: String) {
        // when
        val actual = underTest.convert(turnNotation)

        // then
        assertThat(actual.isLeft()).isTrue
        actual.onLeft { assertThat(it).isEqualTo("Row '${turnNotation[1]}' doesn't exists") }
    }

    @ParameterizedTest
    @CsvSource("a1,1,1", "h8,8,8", "a8,8,1", "h1,1,8")
    fun `makeTurn test when source cell is valid`(turnNotation: String, row: Int, column: Int) {
        // when
        val actual = underTest.convert(turnNotation)

        // then
        assertThat(actual.isRight()).isTrue
        actual.onRight { assertThat(it).isEqualTo(row to column) }
    }
}
