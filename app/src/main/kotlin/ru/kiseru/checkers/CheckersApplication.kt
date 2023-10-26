package ru.kiseru.checkers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CheckersApplication

fun main(args: Array<String>) {
    runApplication<CheckersApplication>(*args)
}
