package ru.kiseru.checkers.model

import java.util.UUID

class User(
    val name: String,
) {

    lateinit var id: UUID

    lateinit var color: Color

    override fun toString(): String =
        name
}
