package com.checkers.servlets;

import com.checkers.board.CheckerBoard;
import com.checkers.exceptions.CheckersException;
import com.checkers.user.User;
import com.checkers.utils.Colour;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
    private CheckerBoard board;
    private User firstPlayer;
    private User secondPlayer;
    private User turn;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("views/game.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String message = "";

        if (board == null) board = new CheckerBoard();

        if (firstPlayer == null) {
            firstPlayer = new User(login, Colour.WHITE, board);
            turn = firstPlayer;
        }
        else if (secondPlayer == null && !login.equals(firstPlayer.getName())) secondPlayer = new User(login, Colour.BLACK, board);

        if (from != null && to != null) {
            if (login.equals(firstPlayer.getName())) {
                try {
                    firstPlayer.makeTurn(from, to);
                    turn = secondPlayer;
                } catch (CheckersException ex) {
                    message = ex.getMessage();
                }
            } else if (login.equals(secondPlayer.getName())) {
                try {
                    secondPlayer.makeTurn(from, to);
                    turn = firstPlayer;
                } catch (CheckersException ex) {
                    message = ex.getMessage();
                }
            }
        }

        req.setAttribute("turn", turn);
        req.setAttribute("firstPlayer", firstPlayer);
        req.setAttribute("secondPlayer", secondPlayer);
        req.setAttribute("message", message);
        req.setAttribute("board", board);
        req.getRequestDispatcher("views/game.jsp").forward(req, resp);
    }
}
