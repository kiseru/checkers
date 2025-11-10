package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import java.util.UUID
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttribute
import ru.kiseru.checkers.service.BoardService
import ru.kiseru.checkers.model.Room
import ru.kiseru.checkers.model.User
import ru.kiseru.checkers.repository.UserRepository
import ru.kiseru.checkers.service.RoomService

@Controller
@RequestMapping("/game")
class GameController(
    private val boardService: BoardService,
    private val userRepository: UserRepository,
    private val roomService: RoomService,
) {

    private val logger = LoggerFactory.getLogger(GameController::class.java)

    @GetMapping
    fun getGamePage(
        @SessionAttribute(name = "uid", required = false) uid: UUID?,
        @SessionAttribute(name = "roomId", required = false) roomId: Int?,
        @RequestParam(name = "from", required = false) from: String?,
        @RequestParam(name = "to", required = false) to: String?,
        session: HttpSession,
        model: Model,
    ): String {
        if (uid == null) {
            logger.warn("Unauthorized access to /game. Missing uid in session.")
            return "redirect:/login"
        }

        val currentUser = userRepository.findUser(uid)
            ?: run {
                logger.warn("User with uid=$uid not found in repository.")
                return "redirect:/login"
            }

        if (roomId == null) {
            logger.warn("Missing roomId in session for user $uid.")
            return "redirect:/find-room"
        }

        val currentRoom = roomService.findOrCreateRoomById(roomId)
            .also { logger.debug("Retrieved room $roomId for user $uid") }

        if (boardService.isGameFinished(currentRoom.board)) {
            val winner = roomService.getTurnOwner(currentRoom)
            session.setAttribute("winner", winner)
            logger.info("Game in room $roomId finished. Winner: $winner. Redirecting to /finish.")
            return "redirect:/finish"
        }

        try {
            roomService.makeTurn(currentRoom, currentUser, from, to)
            logger.info("User $uid made move from $from to $to in room $roomId.")
        } catch (e: Exception) {
            logger.error("Invalid move by user $uid: $from → $to. Reason: ${e.message}")
            model.addAttribute("error", "Invalid move: ${e.message}")
        }

        initModel(model, currentUser, currentRoom)
        logger.info("Rendering game page for room $roomId, user $uid.")
        return "game"
    }

    private fun initModel(model: Model, user: User, room: Room) {
        model.addAllAttributes(
            mapOf(
                "board" to room.board.board,
                "firstPlayer" to room.whitePlayer,
                "secondPlayer" to room.blackPlayer,
                "turn" to roomService.getTurnOwner(room),
                "room" to room,
                "login" to user.name
            )
        )
    }
}
