package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
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
@RequestMapping("login")
class LoginController(
    private val loginService: LoginService,
    private val jwtService: JwtService,
) {

    @GetMapping
    fun getLoginPage() =
        "login"

    @PostMapping
    @ResponseBody
    fun login(@RequestBody loginRq: LoginRq, httpSession: HttpSession): LoginRs {
        val username = loginRq.username ?: ""
        if (username.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required")
        }

        val user = loginService.login(username)
        httpSession.setAttribute("uid", user.id)
        val authToken = jwtService.generateAuthToken(username)
        return LoginRs(authToken)
    }
}
