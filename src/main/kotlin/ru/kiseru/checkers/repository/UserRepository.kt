package ru.kiseru.checkers.repository

import ru.kiseru.checkers.model.User
import java.util.UUID

interface UserRepository {

    fun findUser(userId: UUID): User?

    /**
     * Находит пользователя по имени
     *
     * @param name имя пользователя
     * @return пользователь или null, если пользователь с таким именем не найден
     */
    fun findUserByName(name: String): User?

    /**
     * Создает пользователя с именем [name]
     *
     * @param name имя пользователя
     * @return новый пользователь
     */
    fun createUserWithName(name: String): User
}
