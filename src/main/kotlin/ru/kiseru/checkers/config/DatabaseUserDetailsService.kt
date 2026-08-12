package ru.kiseru.checkers.config

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.kiseru.checkers.repository.UserRepository

/**
 * Реализация [UserDetailsService] на основе [UserRepository].
 *
 * Возвращает [UserDetails] для пользователя с заданным логином, если такой
 * пользователь существует в репозитории.
 */
@Service
class DatabaseUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    /**
     * Загружает пользователя по имени.
     *
     * @param username логин пользователя
     * @return [UserDetails] найденного пользователя
     * @throws UsernameNotFoundException если пользователь с таким логином не найден
     */
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByName(username)
            ?: throw UsernameNotFoundException("User $username not found")

        return User.withUsername(user.name)
            .password("")
            .authorities(emptyList())
            .build()
    }
}