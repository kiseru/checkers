package ru.kiseru.checkers.web.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("finish")
class FinishController {

    @GetMapping
    fun getFinishPage(): String =
        "finish"
}
