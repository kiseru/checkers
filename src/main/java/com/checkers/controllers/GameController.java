package com.checkers.controllers;

import com.checkers.room.Room;
import com.checkers.room.RoomService;
import com.checkers.user.User;
import com.checkers.utils.Color;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequiredArgsConstructor
@Controller
@RequestMapping("game")
public class GameController {

    private final RoomService roomService;

    @GetMapping
    public String getGamePage(
            @SessionAttribute(required = false) String login,
            @SessionAttribute(required = false) Integer roomId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            HttpSession session,
            Model model
    ) {
        if (login == null || login.isEmpty()) {
            return "redirect:/login";
        }

        if (roomId == null) {
            return "redirect:/find-room";
        }

        Room currentRoom = roomService.findOrCreateRoomById(roomId);
        var board = currentRoom.getBoard();

        if (currentRoom.getFirstPlayer() == null) {
            var firstPlayer = new User(login, Color.WHITE, board);
            currentRoom.setFirstPlayer(firstPlayer);
            currentRoom.setTurn(firstPlayer);
        } else if (currentRoom.getSecondPlayer() == null && !login.equals(currentRoom.getFirstPlayer().getName())) {
            var secondPlayer = new User(login, Color.BLACK, board);
            currentRoom.setSecondPlayer(secondPlayer);
        }

        var turn = currentRoom.getTurn();
        if (!board.isGaming()) {
            session.setAttribute("winner", turn.toString());
            return "redirect:/finish";
        }

        var firstPlayer = currentRoom.getFirstPlayer();
        var secondPlayer = currentRoom.getSecondPlayer();
        if (from != null && to != null && secondPlayer != null) {
            if (login.equals(firstPlayer.getName()) && turn.equals(firstPlayer)) {
                firstPlayer.makeTurn(from, to);
                if (!firstPlayer.isCanEat()) {
                    currentRoom.setTurn(secondPlayer);
                }
            } else if (login.equals(secondPlayer.getName()) && turn.equals(secondPlayer)) {
                secondPlayer.makeTurn(from, to);
                if (!secondPlayer.isCanEat()) {
                    currentRoom.setTurn(firstPlayer);
                }
            }
        }


        model.addAttribute("board", board.getBoard());
        model.addAttribute("firstPlayer", firstPlayer);
        model.addAttribute("secondPlayer", secondPlayer);
        model.addAttribute("turn", turn);
        model.addAttribute("room", currentRoom);
        model.addAttribute("login", login);

        return "game";
    }
}
