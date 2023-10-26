package ru.kiseru.checkers.operational.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.domain.user.User
import ru.kiseru.checkers.domain.user.UserRepository
import ru.kiseru.checkers.operational.LoginUseCase

@Component
class LoginUseCaseImpl(
    private val userRepository: UserRepository,
) : LoginUseCase {

    override fun login(name: String): User {
        val user = User(name)
        userRepository.save(user)
        return user
    }
}