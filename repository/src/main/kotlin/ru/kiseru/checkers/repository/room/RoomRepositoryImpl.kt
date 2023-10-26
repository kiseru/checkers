package ru.kiseru.checkers.repository.room

import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.room.RoomRepository

@Component
class RoomRepositoryImpl : RoomRepository {

    private val roomStorage: MutableMap<Int, Room> = ConcurrentHashMap()

    override fun findRoom(roomId: Int): Room? =
        roomStorage[roomId]

    override fun save(room: Room) {
        val roomId = room.id
        roomStorage[roomId] = room
    }
}
