package ru.kiseru.checkers.operational.impl

import org.springframework.stereotype.Component
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.room.RoomRepository
import ru.kiseru.checkers.operational.FindOrCreateRoomUseCase

@Component
class FindOrCreateRoomUseCaseImpl(
    private val roomRepository: RoomRepository,
) : FindOrCreateRoomUseCase {

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
