package ru.kiseru.checkers.converter.impl

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test

class CheckersCellNotationConverterTest {

    private val underTest = CheckersCellNotationConverter()

    @Test
    fun `should convert a1 to (1, 1)`() {
        // when
        val result = underTest.convert("a1")

        // then
        assertThat(result).isEqualTo(1 to 1)
    }

    @Test
    fun `should convert h8 to (8, 8)`() {
        // when
        val result = underTest.convert("h8")

        // then
        assertThat(result).isEqualTo(8 to 8)
    }

    @Test
    fun `should convert d4 to (4, 4)`() {
        // when
        val result = underTest.convert("d4")

        // then
        assertThat(result).isEqualTo(4 to 4)
    }

    @Test
    fun `should convert b3 to (3, 2)`() {
        // when
        val result = underTest.convert("b3")

        // then
        assertThat(result).isEqualTo(3 to 2)
    }

    @Test
    fun `should throw exception for empty string`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("") }
            .withMessage("Invalid notation length: ''. Expected 2 characters.")
    }

    @Test
    fun `should throw exception for single character`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("a") }
            .withMessage("Invalid notation length: 'a'. Expected 2 characters.")
    }

    @Test
    fun `should throw exception for three characters`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("a1x") }
            .withMessage("Invalid notation length: 'a1x'. Expected 2 characters.")
    }

    @Test
    fun `should throw exception for column before a`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("`1") }
            .withMessage("Invalid column: '`'. Must be between a and h.")
    }

    @Test
    fun `should throw exception for column after h`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("i1") }
            .withMessage("Invalid column: 'i'. Must be between a and h.")
    }

    @Test
    fun `should throw exception for uppercase column`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("A1") }
            .withMessage("Invalid column: 'A'. Must be between a and h.")
    }

    @Test
    fun `should throw exception for non-letter column`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("#1") }
            .withMessage("Invalid column: '#'. Must be between a and h.")
    }

    @Test
    fun `should throw exception for row before 1`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("a0") }
            .withMessage("Invalid row: '0'. Must be between 1 and 8.")
    }

    @Test
    fun `should throw exception for row after 8`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("a9") }
            .withMessage("Invalid row: '9'. Must be between 1 and 8.")
    }

    @Test
    fun `should throw exception for non-digit row`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("ab") }
            .withMessage("Invalid row: 'b'. Must be between 1 and 8.")
    }

    @Test
    fun `should throw exception when both column and row are invalid`() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { underTest.convert("i9") }
            .withMessage("Invalid column: 'i'. Must be between a and h.")
    }
}
