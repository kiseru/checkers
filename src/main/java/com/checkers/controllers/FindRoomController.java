package com.checkers.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("find-room")
public class FindRoomController {

    @GetMapping
    public String getFindRoomPage(HttpServletRequest req) {
        var session = req.getSession();
        var login = (String) session.getAttribute("login");
        if (!StringUtils.hasText(login)) {
            return "redirect:/login";
        }

        return "find-room";
    }

    @PostMapping
    protected String findRoom(HttpServletRequest req) {
        var session = req.getSession();
        var login = (String) session.getAttribute("login");
        if (login == null || login.isEmpty()) {
            return "redirect:/login";
        }

        var roomId = req.getParameter("roomId");
        if (roomId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID is required");
        }

        session.setAttribute("roomId", Integer.valueOf(roomId));

        return "redirect:/game";
    }
}
