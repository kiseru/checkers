package ru.kiseru.checkers.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/finish")
class FinishController {

    private val logger = LoggerFactory.getLogger(FinishController::class.java)

    @GetMapping
    fun getFinishPage(): String {
        logger.info("Rendering refresh page")
        return "finish"
    }
}
