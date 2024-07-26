package ru.kiseru.checkers.repository

import ru.kiseru.checkers.model.Room

interface RoomRepository {

    fun findRoom(roomId: Int): Room?

    fun save(room: Room)
}

