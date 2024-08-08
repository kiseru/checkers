package ru.kiseru.checkers.initializer

import ru.kiseru.checkers.model.Board

interface BoardInitializer {

    fun initialize(board: Board)
}

