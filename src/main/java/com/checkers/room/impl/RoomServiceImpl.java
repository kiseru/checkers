package com.checkers.room.impl;

import com.checkers.board.Board;
import com.checkers.room.Room;
import com.checkers.room.RoomService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomServiceImpl implements RoomService {

    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room findOrCreateRoomById(int roomId) {
        return rooms.computeIfAbsent(roomId, this::createRoom);
    }

    private Room createRoom(int roomId) {
        var board = new Board();
        return new Room(roomId, board);
    }
}
