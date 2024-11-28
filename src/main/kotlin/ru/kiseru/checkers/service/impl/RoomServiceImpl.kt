package ru.kiseru.checkers.service.impl

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.right
import org.springframework.stereotype.Component
import ru.kiseru.checkers.converter.CellNotationConverter
import ru.kiseru.checkers.error.ChessError
import ru.kiseru.checkers.model.Board
import ru.kiseru.checkers.initializer.BoardInitializer
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.model.Room
import ru.kiseru.checkers.model.User
import ru.kiseru.checkers.repository.RoomRepository
import ru.kiseru.checkers.service.BoardService
import ru.kiseru.checkers.service.RoomService
import java.util.UUID

@Component
class RoomServiceImpl(
    private val roomRepository: RoomRepository,
    private val boardService: BoardService,
    private val cellNotationConverter: CellNotationConverter,
    private val boardInitializer: BoardInitializer,
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
        val board = Board(UUID.randomUUID())
        boardInitializer.initialize(board)
        return Room(roomId, board)
    }

    override fun makeTurn(room: Room, user: User, from: String?, to: String?): Either<ChessError.CannotEat, Unit> {
        if (room.whitePlayer == null) {
            addPlayer(room, user, Color.WHITE)
            return Unit.right()
        }

        if (room.blackPlayer == null) {
            if (room.whitePlayer?.id != user.id) {
                addPlayer(room, user, Color.BLACK)
            }

            return Unit.right()
        }

        if (!isCurrentUserTurn(user, room)) {
            return Unit.right()
        }

        if (from == null || to == null) {
            return Unit.right()
        }

        val source = cellNotationConverter.convert(from)
        val destination = cellNotationConverter.convert(to)
        boardService.makeTurn(room.board, user.color, source, destination)
            .onRight {
                if (!it) {
                    room.turn = getEnemy(user, room)
                }
            }
        return Unit.right()
    }

    override fun addPlayer(room: Room, user: User, color: Color) {
        when (color) {
            Color.WHITE -> room.whitePlayer = user
            Color.BLACK -> room.blackPlayer = user
        }
        user.color = color
    }

    private fun isCurrentUserTurn(user: User, room: Room): Boolean {
        val turn = getTurnOwner(room)
        return turn?.id == user.id
    }

    override fun getTurnOwner(room: Room): User? =
        when (room.turn) {
            Color.WHITE -> room.whitePlayer
            Color.BLACK -> room.blackPlayer
        }

    private fun getEnemy(user: User, room: Room): Color =
        if (room.whitePlayer!!.id == user.id) {
            Color.BLACK
        } else if (room.blackPlayer!!.id == user.id) {
            Color.WHITE
        } else {
            throw IllegalStateException()
        }
}
