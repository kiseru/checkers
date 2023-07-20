package com.checkers.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("finish")
public class FinishController {

    @PostMapping
    public String getFinishPage() {
        return "finish";
    }
}
