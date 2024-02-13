package ru.kiseru.checkers.web.controllers.dto

data class BoardDto(
    var version: Int,
    var pieces: Sequence<PieceDto>,
)