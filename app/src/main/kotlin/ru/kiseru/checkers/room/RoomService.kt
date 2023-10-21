package ru.kiseru.checkers.room

import ru.kiseru.checkers.domain.room.Room

interface RoomService {

    fun findOrCreateRoomById(roomId: Int): Room
}