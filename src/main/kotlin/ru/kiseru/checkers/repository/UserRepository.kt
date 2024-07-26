package ru.kiseru.checkers.repository

import ru.kiseru.checkers.model.User
import java.util.UUID

interface UserRepository {

    fun findUser(userId: UUID): User?

    fun save(user: User)
}
