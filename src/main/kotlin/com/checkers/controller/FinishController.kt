package com.checkers.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("finish")
class FinishController {

    @PostMapping
    fun getFinishPage(): String =
        "finish"
}
