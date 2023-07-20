package com.checkers.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("find-room")
public class FindRoomController {

    @GetMapping
    public String getFindRoomPage(@SessionAttribute(required = false) String login) {
        if (!StringUtils.hasText(login)) {
            return "redirect:/login";
        }

        return "find-room";
    }

    @PostMapping
    protected String findRoom(@RequestParam Integer roomId, @SessionAttribute String login, HttpSession session) {
        if (login == null || login.isEmpty()) {
            return "redirect:/login";
        }

        if (roomId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID is required");
        }

        session.setAttribute("roomId", roomId);

        return "redirect:/game";
    }
}
