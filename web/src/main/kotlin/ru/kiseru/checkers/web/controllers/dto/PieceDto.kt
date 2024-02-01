package ru.kiseru.checkers.web.controllers.dto

import ru.kiseru.checkers.domain.board.PieceType
import ru.kiseru.checkers.domain.utils.Color

data class PieceDto(
    var cell: String,
    var color: Color,
    var type: PieceType,
)
