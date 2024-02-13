package ru.kiseru.checkers.operational

import ru.kiseru.checkers.domain.board.Board

interface BoardSearchByRoomIdService {

    fun find(roomId: Int): Board?
}
