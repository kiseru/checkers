package ru.kiseru.checkers.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import ru.kiseru.checkers.domain.user.User

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
