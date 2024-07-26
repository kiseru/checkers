package ru.kiseru.checkers.service.impl

import org.springframework.stereotype.Service
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.repository.RoomRepository
import ru.kiseru.checkers.service.BoardSearchByRoomIdService

@Service
class BoardSearchByRoomIdServiceImpl(
    private val roomRepository: RoomRepository,
) : BoardSearchByRoomIdService {

    override fun find(roomId: Int): Board? =
        roomRepository.findRoom(roomId)?.board
}
