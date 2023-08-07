package com.checkers.room

interface RoomService {

    fun findOrCreateRoomById(roomId: Int): Room
}