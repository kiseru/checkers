package ru.kiseru.checkers.domain.user

import ru.kiseru.checkers.domain.utils.Color
import java.util.UUID

class User(
    val name: String,
) {

    lateinit var id: UUID

    lateinit var color: Color

    override fun toString(): String =
        name
}
