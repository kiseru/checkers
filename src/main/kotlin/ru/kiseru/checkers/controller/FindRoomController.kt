package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("find-room")
class FindRoomController {

    @GetMapping
    fun getFindRoomPage(@SessionAttribute login: String?): String =
        if (StringUtils.hasText(login)) {
            "find-room"
        } else {
            "redirect:/login"
        }

    @PostMapping
    fun findRoom(@RequestParam roomId: Int?, @SessionAttribute login: String?, session: HttpSession): String {
        if (login.isNullOrEmpty()) {
            return "redirect:/login"
        }

        if (roomId == null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID is required")
        }

        session.setAttribute("roomId", roomId)

        return "redirect:/game"
    }
}