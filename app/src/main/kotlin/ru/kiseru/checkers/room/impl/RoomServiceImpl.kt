package ru.kiseru.checkers.room.impl

import org.springframework.stereotype.Service
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.room.RoomService
import java.util.concurrent.ConcurrentHashMap

@Service
class RoomServiceImpl : RoomService {

    private val rooms: MutableMap<Int, Room> = ConcurrentHashMap()

    override fun findOrCreateRoomById(roomId: Int): Room =
        rooms.computeIfAbsent(roomId) { Room(roomId, Board()) }
}