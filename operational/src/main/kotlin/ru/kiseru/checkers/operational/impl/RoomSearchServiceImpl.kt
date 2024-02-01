package ru.kiseru.checkers.operational.impl

import org.springframework.stereotype.Service
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.room.RoomRepository
import ru.kiseru.checkers.operational.RoomSearchService

@Service
class RoomSearchServiceImpl(
    private val roomRepository: RoomRepository,
) : RoomSearchService {

    override fun find(roomId: Int): Room? =
        roomRepository.findRoom(roomId)
}
