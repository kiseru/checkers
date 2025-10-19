package ru.kiseru.checkers.model

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
) {

    lateinit var color: Color

    override fun toString(): String =
        name
}
