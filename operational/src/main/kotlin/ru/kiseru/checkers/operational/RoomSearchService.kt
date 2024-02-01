package ru.kiseru.checkers.operational

import ru.kiseru.checkers.domain.room.Room

interface RoomSearchService {

    fun find(roomId: Int): Room?
}
