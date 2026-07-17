package ru.kiseru.checkers.repository

import ru.kiseru.checkers.model.Room
import java.util.UUID

interface RoomRepository {

    fun findRoom(roomId: UUID): Room?

    fun save(room: Room)

    fun findRoomsWithAvailableSlot(): List<Room>
}

