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

    override fun save(user: User) {
        val userId = UUID.randomUUID()
        userStorage[userId] = user
        user.id = userId
    }
}
