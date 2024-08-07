package ru.kiseru.checkers.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.service.BoardService
import ru.kiseru.checkers.utils.getCellCaption
import ru.kiseru.checkers.service.BoardSearchByRoomIdService
import ru.kiseru.checkers.controller.dto.BoardDto
import ru.kiseru.checkers.controller.dto.PieceDto
import kotlin.concurrent.thread

@RequestMapping("room")
@RestController
class RoomController(
    private val boardService: BoardService,
    private val boardSearchByRoomIdService: BoardSearchByRoomIdService,
) {

    @GetMapping("{roomId}/board")
    fun getRoomBoard(@PathVariable roomId: Int, @RequestParam version: Int): DeferredResult<BoardDto> {
        val board = boardSearchByRoomIdService.find(roomId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        val result = DeferredResult<BoardDto>(15000)
        thread {
            boardService.waitNewVersion(board, version)
            val pieces = board.piecesCoordinates()
                .map {
                    val piece = board.getPiece(it)!!
                    PieceDto(getCellCaption(it.first, it.second), piece.color, piece.pieceStrategy.type)
                }
            val boardDto = BoardDto(board.version, pieces)
            result.setResult(boardDto)
        }
        return result
    }
}
