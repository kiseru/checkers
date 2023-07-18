package com.checkers.servlets;

import com.checkers.board.Board;
import com.checkers.exceptions.CheckersException;
import com.checkers.game.Room;
import com.checkers.user.User;
import com.checkers.utils.Color;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/game")
public class GameServlet extends HttpServlet {

    private final List<Room> rooms = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var session = req.getSession();

            var login = (String) session.getAttribute("login");
            if (login == null || login.isEmpty()) {
                resp.sendRedirect("/login");
                return;
            }

            var roomId = (int) session.getAttribute("roomId");

            String from = req.getParameter("from");
            String to = req.getParameter("to");


            Room currentRoom = findOrCreateRoom(roomId);
            var board = currentRoom.getBoard();

            if (currentRoom.getFirstPlayer() == null) {
                var firstPlayer = new User(login, Color.WHITE, board);
                currentRoom.setFirstPlayer(firstPlayer);
                currentRoom.setTurn(firstPlayer);
            } else if (currentRoom.getSecondPlayer() == null && !login.equals(currentRoom.getFirstPlayer().getName())) {
                var secondPlayer = new User(login, Color.BLACK, board);
                currentRoom.setSecondPlayer(secondPlayer);
            }

            if (!board.isGaming()) {
                req.setAttribute("winner", currentRoom.getTurn().toString());
                rooms.remove(currentRoom);
                req.getRequestDispatcher("/finish").forward(req, resp);
                return;
            }

            var firstPlayer = currentRoom.getFirstPlayer();
            var secondPlayer = currentRoom.getSecondPlayer();
            if (from != null && to != null && secondPlayer != null) {
                if (login.equals(firstPlayer.getName()) && currentRoom.getTurn().equals(firstPlayer)) {
                    firstPlayer.makeTurn(from, to);
                    if (!firstPlayer.isCanEat()) {
                        currentRoom.setTurn(secondPlayer);
                    }
                } else if (login.equals(secondPlayer.getName()) && currentRoom.getTurn().equals(secondPlayer)) {
                    secondPlayer.makeTurn(from, to);
                    if (!secondPlayer.isCanEat()) {
                        currentRoom.setTurn(firstPlayer);
                    }
                }
            }

            req.setAttribute("room", currentRoom);

            req.getRequestDispatcher("views/game.jsp").forward(req, resp);

        } catch (CheckersException e) {
            resp.sendError(500, e.getMessage());
        }
    }

    private Room findOrCreateRoom(int roomId) {
        return rooms.stream()
                .filter(room -> room.getId() == roomId)
                .findFirst()
                .orElseGet(() -> createNewRoom(roomId));
    }

    private Room createNewRoom(int roomId) {
        var room = new Room(roomId, new Board());
        rooms.add(room);
        return room;
    }
}
