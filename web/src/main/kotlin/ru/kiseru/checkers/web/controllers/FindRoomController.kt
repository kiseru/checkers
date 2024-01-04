package ru.kiseru.checkers.web.controllers

import jakarta.servlet.http.HttpSession
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
@RequestMapping("find-room")
class FindRoomController {

    @GetMapping
    fun getFindRoomPage(@SessionAttribute uid: UUID?): String =
        if (uid == null) {
            "redirect:/login"
        } else {
            "find-room"
        }

    @PostMapping
    fun findRoom(@RequestParam roomId: Int?, @SessionAttribute uid: UUID?, session: HttpSession): String {
        if (uid == null) {
            return "redirect:/login"
        }

        if (roomId == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID is required")
        }

        session.setAttribute("roomId", roomId)

        return "redirect:/game"
    }
}
