package ru.kiseru.checkers.domain.user

import java.util.UUID

interface UserRepository {

    fun findUser(userId: UUID): User?

    fun save(user: User)
}
