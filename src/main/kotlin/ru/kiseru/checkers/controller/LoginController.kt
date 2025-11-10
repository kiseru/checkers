package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.service.LoginService

@Controller
@RequestMapping("/login")
class LoginController(
    private val loginService: LoginService,
) {

    private val logger = LoggerFactory.getLogger(LoginController::class.java)

    @GetMapping
    fun getLoginPage(): String {
        logger.info("Rendering login page")
        return "login"
    }

    @PostMapping
    fun login(
        @RequestParam(name = "login", required = true) login: String,
        httpSession: HttpSession,
    ): String {
        if (login.isBlank()) {
            logger.warn("Empty login attempt")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Login is required")
        }

        logger.info("Login attempt for user: $login")

        val user = loginService.login(login)
            .also { logger.info("User $login authenticated successfully. User ID: ${it.id}") }

        httpSession.setAttribute("uid", user.id)
        logger.info("Session created for user $login with uid=${user.id}")

        return "redirect:/find-room"
    }
}
