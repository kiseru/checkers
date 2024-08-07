package ru.kiseru.checkers.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserTest {

    private val name = "Some cool name"

    private val underTest = User(name)

    @Test
    fun `toString test`() {
        // when
        val actual = underTest.toString()

        // then
        assertThat(actual).isEqualTo(name)
    }
}
