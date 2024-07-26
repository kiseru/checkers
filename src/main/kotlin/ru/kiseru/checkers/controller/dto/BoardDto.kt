package ru.kiseru.checkers.controller.dto

data class BoardDto(
    var version: Int,
    var pieces: Sequence<PieceDto>,
)
