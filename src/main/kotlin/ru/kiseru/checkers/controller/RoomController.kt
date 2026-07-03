package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.service.BoardService
import ru.kiseru.checkers.utils.getCellCaption
import ru.kiseru.checkers.service.BoardSearchByRoomIdService
import ru.kiseru.checkers.controller.dto.BoardDto
import ru.kiseru.checkers.controller.dto.PieceDto
import java.util.*
import java.util.concurrent.ExecutorService

@RequestMapping("/room")
@Controller
class RoomController(
    private val boardService: BoardService,
    private val boardSearchByRoomIdService: BoardSearchByRoomIdService,
    private val executor: ExecutorService,
) {

    private val logger = LoggerFactory.getLogger(RoomController::class.java)

    @GetMapping("/create")
    fun getCreateRoomPage(@SessionAttribute uid: UUID?): String =
        if (uid == null) {
            logger.warn("Unauthorized access to /room/create page. Redirecting to login.")
            "redirect:/login"
        } else {
            logger.info("User $uid accessed /room/create page.")
            "room/create"
        }

    @PostMapping("/create")
    fun findRoom(
        @RequestParam("roomId") roomId: Int?,
        @SessionAttribute("uid") uid: UUID?,
        session: HttpSession,
    ): String {
        if (uid == null) {
            logger.warn("Unauthorized attempt to join room. Missing uid in session.")
            return "redirect:/login"
        }

        logger.info("User $uid attempting to join room with roomId=$roomId")

        val validatedRoomId = roomId ?: run {
            logger.error("Missing roomId parameter in request for user $uid")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID is required")
        }

        logger.debug("Validated room=$validatedRoomId for user $uid")

        session.setAttribute("roomId", validatedRoomId)
        logger.info("User $uid successfully joined room $validatedRoomId. Stored in session with sessionId=${session.id}")

        return "redirect:/game"
    }

    @ResponseBody
    @GetMapping("{roomId}/board")
    fun getRoomBoard(
        @PathVariable("roomId") roomId: Int,
        @RequestParam("version") version: Int,
    ): DeferredResult<BoardDto> {
        val board = boardSearchByRoomIdService.find(roomId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Room with ID $roomId not found",
            )

        logger.debug("Found board for room $roomId, current version: ${board.version}")

        val result = DeferredResult<BoardDto>(15000)
            .also {
                it.onTimeout {
                    logger.warn("Request timeout for room $roomId, version $version")
                    it.setErrorResult(ResponseStatusException(HttpStatus.REQUEST_TIMEOUT))
                }
                it.onError { e ->
                    logger.error("Error processing request for room $roomId: ${e.message}", e)
                    it.setErrorResult(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"))
                }
            }

        executor.submit {
            try {
                boardService.waitNewVersion(board, version)
                val pieces = board.piecesCoordinates()
                    .map { coordinate ->
                        val piece = board.getPiece(coordinate)
                            ?: throw IllegalArgumentException("Piece not found at $coordinate")

                        PieceDto(
                            cell = getCellCaption(coordinate.first, coordinate.second),
                            color = piece.color,
                            type = piece.pieceStrategy.type,
                        )
                    }

                val boardDto = BoardDto(
                    version = board.version,
                    pieces = pieces,
                )

                logger.info("Successfully processed board for room $roomId, new version: ${boardDto.version}")
                result.setResult(boardDto)
            } catch (e: InterruptedException) {
                logger.warn("Request interrupted for room $roomId", e)
                result.setErrorResult(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
            } catch (e: Exception) {
                logger.error("Unexpected error for room $roomId: ${e.message}", e)
                result.setErrorResult(
                    ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
                )
            }
        }

        return result
    }
}