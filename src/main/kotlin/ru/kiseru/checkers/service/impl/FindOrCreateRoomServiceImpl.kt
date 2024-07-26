package ru.kiseru.checkers.service.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.model.Room
import ru.kiseru.checkers.repository.RoomRepository
import ru.kiseru.checkers.service.FindOrCreateRoomService
import java.util.*

@Component
class FindOrCreateRoomServiceImpl(
    private val roomRepository: RoomRepository,
) : FindOrCreateRoomService {

    override fun findOrCreateRoomById(roomId: Int): Room {
        val room = roomRepository.findRoom(roomId)
        if (room != null) {
            return room
        }

        val newRoom = createRoom(roomId)
        roomRepository.save(newRoom)
        return newRoom
    }

    private fun createRoom(roomId: Int): Room {
        val board = Board(UUID.randomUUID())
        return Room(roomId, board)
    }
}
