package com.checkers.controller

import com.checkers.room.RoomService
import com.checkers.user.User
import com.checkers.utils.Color
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttribute

@Controller
@RequestMapping("game")
class GameController(
    private val roomService: RoomService,
) {

    @GetMapping
    fun getGamePage(
        @SessionAttribute(required = false) login: String?,
        @SessionAttribute(required = false) roomId: Int?,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) to: String?,
        session: HttpSession,
        model: Model,
    ): String {
        if (login.isNullOrEmpty()) {
            return "redirect:/login"
        }

        if (roomId == null) {
            return "redirect:/find-room"
        }

        val currentRoom = roomService.findOrCreateRoomById(roomId)

        if (currentRoom.firstPlayer == null) {
            val firstPlayer = User(login, Color.WHITE, currentRoom.board)
            currentRoom.firstPlayer = firstPlayer
            currentRoom.turn = firstPlayer
        } else if (currentRoom.secondPlayer == null && login != currentRoom.firstPlayer?.name) {
            val secondPlayer = User(login, Color.BLACK, currentRoom.board)
            currentRoom.secondPlayer = secondPlayer
        }

        if (!currentRoom.board.isGaming()) {
            session.setAttribute("winner", currentRoom.turn)
            return "redirect:/finish"
        }

        val firstPlayer = currentRoom.firstPlayer
        val secondPlayer = currentRoom.secondPlayer
        if (from != null && to != null && secondPlayer != null) {
            if (login == firstPlayer?.name && currentRoom.turn == firstPlayer) {
                firstPlayer.makeTurn(from, to)
                if (!firstPlayer.isCanEat) {
                    currentRoom.turn = secondPlayer
                }
            } else if (login == secondPlayer.name && currentRoom.turn == secondPlayer) {
                secondPlayer.makeTurn(from, to)
                if (!secondPlayer.isCanEat) {
                    currentRoom.turn = firstPlayer
                }
            }
        }

        model.addAttribute("board", currentRoom.board.board)
        model.addAttribute("firstPlayer", firstPlayer)
        model.addAttribute("secondPlayer", secondPlayer)
        model.addAttribute("turn", currentRoom.turn)
        model.addAttribute("room", currentRoom)
        model.addAttribute("login", login)

        return "game"
    }
}