package com.checkers.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/find-room")
public class FindRoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var requestDispatcher = req.getRequestDispatcher("views/find-room.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var session = req.getSession();
        var login = (String) session.getAttribute("login");
        if (login == null || login.isEmpty()) {
            resp.sendRedirect("/login");
            return;
        }

        var roomId = req.getParameter("room-id");
        if (roomId == null || roomId.isEmpty()) {
            resp.sendError(400, "Room ID is required");
            return;
        }

        session.setAttribute("roomId", Integer.valueOf(roomId));

        resp.sendRedirect("/game");
    }
}
