package ru.kiseru.checkers.domain.room

interface RoomService {

    fun findOrCreateRoomById(roomId: Int): Room
}
