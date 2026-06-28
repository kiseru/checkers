package ru.kiseru.checkers.controller.dto

import ru.kiseru.checkers.model.PieceType
import ru.kiseru.checkers.model.Color

data class PieceDto(
    val cell: String,
    val color: Color,
    val type: PieceType,
)
