package ru.kiseru.checkers.repository.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.kiseru.checkers.repository.UserRepository

class UserRepositoryImplTest {

    private val underTest: UserRepository = UserRepositoryImpl()

    @Test
    fun `findUserByName returns user when user with the name exists`() {
        // given
        val user = underTest.createUserWithName("Player")

        // when
        val actual = underTest.findUserByName("Player")

        // then
        assertThat(actual).isEqualTo(user)
    }

    @Test
    fun `findUserByName returns null when user with the name does not exist`() {
        // given
        underTest.createUserWithName("Player")

        // when
        val actual = underTest.findUserByName("Unknown")

        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `findUserByName returns null when repository is empty`() {
        // when
        val actual = underTest.findUserByName("Nobody")

        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `findUserByName is case sensitive`() {
        // given
        underTest.createUserWithName("Player")

        // when
        val actual = underTest.findUserByName("player")

        // then
        assertThat(actual).isNull()
    }
}