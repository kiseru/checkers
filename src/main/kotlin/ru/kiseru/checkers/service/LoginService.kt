package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.User

interface LoginService {

    fun login(name: String): User
}
