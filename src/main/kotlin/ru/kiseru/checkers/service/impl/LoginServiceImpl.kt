package ru.kiseru.checkers.service.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.model.User
import ru.kiseru.checkers.repository.UserRepository
import ru.kiseru.checkers.service.LoginService

@Component
class LoginServiceImpl(
    private val userRepository: UserRepository,
) : LoginService {

    override fun login(name: String): User =
        userRepository.createUserWithName(name)
}
