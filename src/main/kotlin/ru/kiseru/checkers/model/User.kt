package ru.kiseru.checkers.model

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    var color: Color? = null,
) {

    override fun toString(): String =
        name
}
