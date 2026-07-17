package ru.kiseru.checkers.controller.dto

data class BoardDto(
    val version: Int,
    val pieces: List<PieceDto>,
)
