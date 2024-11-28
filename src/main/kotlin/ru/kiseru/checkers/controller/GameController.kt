package ru.kiseru.checkers.controller

import arrow.core.getOrElse
import jakarta.servlet.http.HttpSession
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
@RequestMapping("game")
class GameController(
    private val boardService: BoardService,
    private val userRepository: UserRepository,
    private val roomService: RoomService,
) {

    @GetMapping
    fun getGamePage(
        @SessionAttribute(required = false) uid: UUID?,
        @SessionAttribute(required = false) roomId: Int?,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) to: String?,
        session: HttpSession,
        model: Model,
    ): String {
        if (uid == null) {
            return "redirect:/login"
        }

        val currentUser = userRepository.findUser(uid) ?: return "redirect:/login"

        if (roomId == null) {
            return "redirect:/find-room"
        }

        val currentRoom = roomService.findOrCreateRoomById(roomId)
        if (boardService.isGameFinished(currentRoom.board)) {
            val winner = roomService.getTurnOwner(currentRoom)
            session.setAttribute("winner", winner)
            return "redirect:/finish"
        }

        roomService.makeTurn(currentRoom, currentUser, from, to)
            .getOrElse { (source, destination) -> throw RuntimeException("Can't eat from $source to $destination") }
        initModel(model, currentUser, currentRoom)
        return "game"
    }

    private fun initModel(model: Model, user: User, room: Room) {
        model.addAttribute("board", room.board.board)
        model.addAttribute("firstPlayer", room.whitePlayer)
        model.addAttribute("secondPlayer", room.blackPlayer)
        val turn = roomService.getTurnOwner(room)
        model.addAttribute("turn", turn)
        model.addAttribute("room", room)
        model.addAttribute("login", user.name)
    }
}
