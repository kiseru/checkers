package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Board

interface BoardSearchByRoomIdService {

    fun find(roomId: Int): Board?
}
