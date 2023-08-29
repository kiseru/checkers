package ru.kiseru.checkers.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class MustEatException : RuntimeException("You must eat enemy piece")
