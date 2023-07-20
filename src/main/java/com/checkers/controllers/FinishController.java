package com.checkers.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("finish")
public class FinishController {

    @PostMapping
    public String getFinishPage(HttpServletRequest req, HttpServletResponse resp) {
        return "finish";
    }
}
