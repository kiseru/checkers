package ru.kiseru.checkers.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class AsyncConfig {

    @Bean
    fun taskExecutor(): ExecutorService {
        return Executors.newFixedThreadPool(10)
    }
}