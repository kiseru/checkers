package ru.kiseru.checkers.web.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.operational.RoomSearchService
import ru.kiseru.checkers.web.controllers.dto.PieceDto

@RequestMapping("room")
@RestController
class RoomController(
    private val roomSearchService: RoomSearchService,
) {

    @GetMapping("{roomId}/piece")
    fun getRoomPieces(@PathVariable roomId: Int): Sequence<PieceDto> {
        val room = roomSearchService.find(roomId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return room.board.pieces()
            .map { PieceDto(it.cell.toString(), it.color, it.type) }
    }
}
