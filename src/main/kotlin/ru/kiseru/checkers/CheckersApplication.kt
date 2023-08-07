package ru.kiseru.checkers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.JstlView

@SpringBootApplication
class CheckersApplication {

    @Bean
    fun viewResolver(): InternalResourceViewResolver =
        InternalResourceViewResolver()
            .also {
                it.setViewClass(JstlView::class.java)
                it.setPrefix("/WEB-INF/jsp/")
                it.setSuffix(".jsp")
            }
}

fun main(args: Array<String>) {
    runApplication<CheckersApplication>(*args)
}
