package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.controller.dto.LoginRq
import ru.kiseru.checkers.controller.dto.LoginRs
import ru.kiseru.checkers.service.JwtService
import ru.kiseru.checkers.service.LoginService

@Controller
@RequestMapping("/login")
class LoginController(
    private val loginService: LoginService,
    private val jwtService: JwtService,
) {

    private val logger = LoggerFactory.getLogger(LoginController::class.java)

    @GetMapping
    fun getLoginPage(): String {
        logger.info("Rendering login page")
        return "login"
    }

    @PostMapping
    @ResponseBody
    fun login(
        @RequestBody loginRq: LoginRq,
        httpSession: HttpSession,
    ): LoginRs {
        val username = loginRq.username?.trim()
        if (username.isNullOrBlank()) {
            logger.warn("Empty or blank username in login request")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required")
        }

        logger.info("Login attempt for user: $username")

        val user = loginService.login(username)

        logger.info("User $username authenticated successfully. User ID: ${user.id}")

        httpSession.invalidate()
        httpSession.setAttribute("uid", user.id)
        logger.info("New session created for user $username with uid=${user.id}")



        val authToken = jwtService.generateAuthToken(username)
            .also { logger.debug("JWT token generated for user $username") }

        return LoginRs(authToken)
    }
}
