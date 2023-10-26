package ru.kiseru.checkers.operational

import ru.kiseru.checkers.domain.user.User

interface LoginUseCase {

    fun login(name: String): User
}