package com.checkers.servlets;

import com.checkers.board.CheckerBoard;
import com.checkers.exceptions.CheckersException;
import com.checkers.game.Room;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private LinkedList<Room> rooms = new LinkedList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        Integer id = Integer.parseInt(req.getParameter("id"));
        String isCreate = req.getParameter("isCreate");
        String message = "";
        boolean isSending = false;


        Room room = null;
        boolean existed = false;
        for (Room roomIterator : rooms) {
            if (roomIterator.getId() == id) {
                existed = true;
                room = roomIterator;
                break;
            }
        }
        if (!existed) {
            room = new Room(id, new CheckerBoard());
            rooms.add(room);
        }

        if (room.getFirstPlayer() == null) {
            room.setFirstPlayer(new User(login, Colour.WHITE, room.getBoard()));
            room.setTurn(room.getFirstPlayer());
        } else if (room.getSecondPlayer() == null && !login.equals(room.getFirstPlayer().getName())) {
            room.setSecondPlayer(new User(login, Colour.BLACK, room.getBoard()));
        }

        if (!room.getBoard().isGaming()) {
            req.setAttribute("winner", room.getTurn().toString());
            rooms.remove(room);
            req.getRequestDispatcher("/finish").forward(req, resp);
        }

        if (from != null && to != null) {
            if (login.equals(room.getFirstPlayer().getName()) && room.getTurn().equals(room.getFirstPlayer())) {
                try {
                    room.getFirstPlayer().makeTurn(from, to);
                    if (!room.getFirstPlayer().isCanEat()) room.setTurn(room.getSecondPlayer());
                } catch (CheckersException ex) {
                    message = ex.getMessage();
                }
            } else if (login.equals(room.getSecondPlayer().getName()) && room.getTurn().equals(room.getSecondPlayer())) {
                try {
                    room.getSecondPlayer().makeTurn(from, to);
                    if (!room.getSecondPlayer().isCanEat()) room.setTurn(room.getFirstPlayer());
                } catch (CheckersException ex) {
                    message = ex.getMessage();
                }
            }
        }

        req.setAttribute("room", room);

        req.getRequestDispatcher("views/game.jsp").forward(req, resp);
    }
}
