package ru.kiseru.checkers.room.impl

import org.springframework.stereotype.Service
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.room.RoomRepository
import ru.kiseru.checkers.room.RoomService

@Service
class RoomServiceImpl(
    private val roomRepository: RoomRepository,
) : RoomService {

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
        val board = Board()
        return Room(roomId, board)
    }
}
