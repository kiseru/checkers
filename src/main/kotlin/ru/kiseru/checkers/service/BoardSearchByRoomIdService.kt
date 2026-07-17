package ru.kiseru.checkers.service

import ru.kiseru.checkers.model.Board
import java.util.UUID

interface BoardSearchByRoomIdService {

    fun find(roomId: UUID): Board?
}
