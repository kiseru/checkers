package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.model.Color
import ru.kiseru.checkers.repository.RoomRepository
import ru.kiseru.checkers.repository.UserRepository
import ru.kiseru.checkers.service.BoardService
import ru.kiseru.checkers.service.RoomService
import ru.kiseru.checkers.utils.getCellCaption
import ru.kiseru.checkers.controller.dto.BoardDto
import ru.kiseru.checkers.controller.dto.PieceDto
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

@RequestMapping("/room")
@Controller
class RoomController(
    private val boardService: BoardService,
    private val roomRepository: RoomRepository,
    private val roomService: RoomService,
    private val userRepository: UserRepository,
    private val executor: ExecutorService,
) {

    private val logger = LoggerFactory.getLogger(RoomController::class.java)

    @GetMapping
    fun getRoomListPage(
        @SessionAttribute uid: UUID?,
        model: Model,
    ): String {
        if (uid == null) {
            logger.warn("Unauthorized access to /rooms page. Redirecting to login.")
            return "redirect:/login"
        }

        logger.info("User $uid accessed /rooms page.")
        val availableRooms = roomService.getAvailableRooms()
        model.addAttribute("rooms", availableRooms)
        return "room/index"
    }

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
    fun createRoom(
        @RequestParam("name") name: String?,
        @RequestParam("color") color: String?,
        @SessionAttribute("uid") uid: UUID?,
        session: HttpSession,
    ): String {
        if (uid == null) {
            logger.warn("Unauthorized attempt to create room. Missing uid in session.")
            return "redirect:/login"
        }

        val validatedName = name?.takeIf { it.isNotBlank() } ?: run {
            logger.error("Missing or empty room name in request for user $uid")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Room name is required")
        }

        logger.info("User $uid creating room with name='$validatedName'")

        val selectedColor = try {
            color?.let { Color.valueOf(it.uppercase()) } ?: Color.WHITE
        } catch (_: IllegalArgumentException) {
            logger.warn("Invalid color value '$color' for user $uid, defaulting to WHITE")
            Color.WHITE
        }

        val room = roomService.createRoom(validatedName)
        logger.info("User $uid created room ${room.id} with name='$validatedName'")

        session.setAttribute("roomId", room.id)
        session.setAttribute("color", selectedColor.name)
        logger.info("User $uid successfully created and joined room ${room.id}. Stored in session with sessionId=${session.id}")

        return "redirect:/game"
    }

    @PostMapping("/{roomId}/join")
    fun joinRoom(
        @PathVariable("roomId") roomId: UUID,
        @SessionAttribute("uid") uid: UUID?,
        session: HttpSession,
    ): String {
        if (uid == null) {
            logger.warn("Unauthorized attempt to join room. Missing uid in session.")
            return "redirect:/login"
        }

        logger.info("User $uid attempting to join room $roomId from room list.")

        val room = roomRepository.findRoom(roomId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Room with ID $roomId not found",
            )

        val user = userRepository.findUser(uid)
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User not found",
            )

        synchronized(room) {
            val availableColor = when {
                room.whitePlayer == null -> Color.WHITE
                room.blackPlayer == null -> Color.BLACK
                else -> throw ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room $roomId is already full",
                )
            }

            roomService.addPlayer(room, user, availableColor)
            session.setAttribute("roomId", roomId)
            session.setAttribute("color", availableColor.name)
            logger.info("User $uid joined room $roomId as $availableColor")
        }

        return "redirect:/game"
    }

    @ResponseBody
    @GetMapping("{roomId}/board")
    fun getRoomBoard(
        @PathVariable("roomId") roomId: UUID,
        @RequestParam("version") version: Int,
    ): DeferredResult<BoardDto> {
        val room = roomRepository.findRoom(roomId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Room with ID $roomId not found",
            )

        val board = synchronized(room) {
            room.board
        }

        logger.debug("Found board for room $roomId, current version: ${board.version}")

        val result = DeferredResult<BoardDto>(15000)
        var future: Future<*>? = null

        result.onTimeout {
            logger.warn("Request timeout for room $roomId, version $version")
            future?.cancel(true)
            result.setErrorResult(ResponseStatusException(HttpStatus.REQUEST_TIMEOUT))
        }
        result.onError { e ->
            logger.error("Error processing request for room $roomId: ${e.message}", e)
            result.setErrorResult(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"))
        }

        future = executor.submit {
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
                    .toList()

                val boardDto = BoardDto(
                    version = board.version,
                    pieces = pieces,
                )

                logger.info("Successfully processed board for room $roomId, new version: ${boardDto.version}")
                result.setResult(boardDto)
            } catch (e: InterruptedException) {
                logger.warn("Request interrupted for room $roomId", e)
                Thread.currentThread().interrupt()
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
