package ru.kiseru.checkers.operational.impl

import org.springframework.stereotype.Service
import ru.kiseru.checkers.domain.board.Board
import ru.kiseru.checkers.domain.room.RoomRepository
import ru.kiseru.checkers.operational.BoardSearchByRoomIdService

@Service
class BoardSearchByRoomIdServiceImpl(
    private val roomRepository: RoomRepository,
) : BoardSearchByRoomIdService {

    override fun find(roomId: Int): Board? =
        roomRepository.findRoom(roomId)?.board
}