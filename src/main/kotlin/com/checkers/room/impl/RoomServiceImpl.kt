package com.checkers.room.impl

import com.checkers.board.Board
import com.checkers.room.Room
import com.checkers.room.RoomService
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class RoomServiceImpl : RoomService {

    private val rooms: MutableMap<Int, Room> = ConcurrentHashMap()

    override fun findOrCreateRoomById(roomId: Int): Room =
        rooms.computeIfAbsent(roomId) { Room(roomId, Board()) }
}