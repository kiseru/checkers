package ru.kiseru.checkers.room

interface RoomService {

    fun findOrCreateRoomById(roomId: Int): Room
}