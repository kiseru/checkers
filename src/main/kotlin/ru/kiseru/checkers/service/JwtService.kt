package ru.kiseru.checkers.service

interface JwtService {

    fun generateAuthToken(username: String): String
}