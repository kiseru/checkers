package ru.kiseru.checkers.repository

import ru.kiseru.checkers.model.User
import java.util.UUID

interface UserRepository {

    fun findUser(userId: UUID): User?

    /**
     * Создает пользователя с именем [name]
     *
     * @param name имя пользователя
     * @return новый пользователь
     */
    fun createUserWithName(name: String): User
}
