package ru.kiseru.checkers.domain.room

interface RoomRepository {

    fun findRoom(roomId: Int): Room?

    fun save(room: Room)
}

