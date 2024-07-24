package ru.kiseru.checkers.web.controllers

import jakarta.servlet.http.HttpSession
import java.lang.IllegalStateException
import java.util.UUID
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttribute
import ru.kiseru.checkers.domain.service.BoardService
import ru.kiseru.checkers.domain.room.Room
import ru.kiseru.checkers.domain.user.User
import ru.kiseru.checkers.domain.user.UserRepository
import ru.kiseru.checkers.domain.utils.Color
import ru.kiseru.checkers.operational.FindOrCreateRoomUseCase

@Controller
@RequestMapping("game")
class GameController(
    private val boardService: BoardService,
    private val userRepository: UserRepository,
    private val findOrCreateRoomUseCase: FindOrCreateRoomUseCase,
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

        val currentRoom = findOrCreateRoomUseCase.findOrCreateRoomById(roomId)
        if (!currentRoom.board.isGaming()) {
            session.setAttribute("winner", currentRoom.turn)
            return "redirect:/finish"
        }

        initModel(model, currentUser, currentRoom)

        if (currentRoom.firstPlayer == null) {
            initWhitePlayer(currentUser, currentRoom)
            model.addAttribute("firstPlayer", currentUser)
            return "game"
        }

        if (currentRoom.secondPlayer == null) {
            if (currentRoom.firstPlayer?.id == currentUser.id) {
                return "game"
            }

            initBlackPlayer(currentUser, currentRoom)
            model.addAttribute("secondPlayer", currentUser)
            return "game"
        }

        if (!isCurrentUserTurn(currentUser, currentRoom)) {
            return "game"
        }

        if (from == null || to == null) {
            return "game"
        }

        val isCanEat = boardService.makeTurn(currentRoom.board, currentUser.color, from, to)
        if (!isCanEat) {
            currentRoom.turn = getEnemy(currentUser, currentRoom)
        }

        return "game"
    }

    private fun initModel(model: Model, user: User, room: Room) {
        model.addAttribute("board", room.board.board)
        model.addAttribute("firstPlayer", room.firstPlayer)
        model.addAttribute("secondPlayer", room.secondPlayer)
        model.addAttribute("turn", room.turn)
        model.addAttribute("room", room)
        model.addAttribute("login", user.name)
    }

    private fun initWhitePlayer(user: User, room: Room) {
        room.firstPlayer = user
        room.turn = user
        user.color = Color.WHITE
    }

    private fun initBlackPlayer(user: User, room: Room) {
        room.secondPlayer = user
        user.color = Color.BLACK
    }

    private fun isCurrentUserTurn(user: User, room: Room): Boolean {
        val turn = room.turn ?: return false
        return turn.id == user.id
    }

    private fun getEnemy(user: User, room: Room): User {
        if (room.firstPlayer!!.id == user.id) {
            return room.secondPlayer!!
        } else if (room.secondPlayer!!.id == user.id) {
            return room.firstPlayer!!
        }

        throw IllegalStateException()
    }
}
