package ru.kiseru.checkers.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Конфигурация Spring Security.
 *
 * Защищает endpoint `/room/{roomId}/board` через Basic Auth, оставляя все
 * остальные endpoint'ы доступными без аутентификации.
 */
@Configuration
class SecurityConfig {

    /**
     * Определяет цепочку фильтров безопасности.
     *
     * @param http объект [HttpSecurity] для построения цепочки фильтров
     * @return сконфигурированная цепочка фильтров безопасности
     * @throws Exception при ошибке конфигурации
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/room/{roomId}/board").authenticated()
                    .anyRequest().permitAll()
            }
            .httpBasic(Customizer.withDefaults())
            .build()

    /**
     * Предоставляет [PasswordEncoder]. Так как приложение аутентифицирует
     * пользователя только по логину (без пароля), используется
     * [NoOpPasswordEncoder].
     *
     * @return [NoOpPasswordEncoder] экземпляр
     */
    @Suppress("deprecation")
    @Bean
    fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()
}