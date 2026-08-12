package ru.kiseru.checkers.repository.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.model.User
import ru.kiseru.checkers.repository.UserRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class UserRepositoryImpl : UserRepository {

    private val userStorage: MutableMap<UUID, User> = ConcurrentHashMap()

    override fun findUser(userId: UUID): User? =
        userStorage[userId]

    /**
     * Находит пользователя по имени
     *
     * @param name имя пользователя
     * @return пользователь или null, если пользователь с таким именем не найден
     */
    override fun findUserByName(name: String): User? =
        userStorage.values.firstOrNull { it.name == name }

    override fun createUserWithName(name: String): User {
        val userId = UUID.randomUUID()
        val user = User(id = userId, name = name)
        userStorage[userId] = user
        return user
    }
}
