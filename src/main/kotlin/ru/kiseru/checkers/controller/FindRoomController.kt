package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Controller
@RequestMapping("/find-room")
class FindRoomController {

    private val logger = LoggerFactory.getLogger(FindRoomController::class.java)

    @GetMapping
    fun getFindRoomPage(@SessionAttribute uid: UUID?): String =
        if (uid == null) {
            logger.warn("Unauthorized access to /find-room page. Redirecting to login.")
            "redirect:/login"
        } else {
            logger.info("User $uid accessed /find-room page.")
            "find-room"
        }

    @PostMapping
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
}
