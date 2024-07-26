package ru.kiseru.checkers.controller.dto

import ru.kiseru.checkers.model.PieceType
import ru.kiseru.checkers.model.Color

data class PieceDto(
    var cell: String,
    var color: Color,
    var type: PieceType,
)
