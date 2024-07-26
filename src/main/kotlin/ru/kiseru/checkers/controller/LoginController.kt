package ru.kiseru.checkers.controller

import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import ru.kiseru.checkers.service.LoginService

@Controller
@RequestMapping("login")
class LoginController(
    private val loginService: LoginService,
) {

    @GetMapping
    fun getLoginPage() =
        "login"

    @PostMapping
    fun login(@RequestParam login: String, httpSession: HttpSession): String {
        if (login.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Login is required")
        }

        val user = loginService.login(login)
        httpSession.setAttribute("uid", user.id)
        return "redirect:/find-room"
    }
}
