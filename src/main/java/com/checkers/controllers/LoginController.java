package com.checkers.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String login, HttpSession httpSession) {
        if (login.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login is required");
        }

        httpSession.setAttribute("login", login);

        return "redirect:/find-room";
    }
}
