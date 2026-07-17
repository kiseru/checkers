package ru.kiseru.checkers.service.impl

import org.springframework.stereotype.Service
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.repository.RoomRepository
import ru.kiseru.checkers.service.BoardSearchByRoomIdService
import java.util.UUID

@Service
class BoardSearchByRoomIdServiceImpl(
    private val roomRepository: RoomRepository,
) : BoardSearchByRoomIdService {

    override fun find(roomId: UUID): Board? =
        roomRepository.findRoom(roomId)?.board
}
