package ru.kiseru.checkers.service.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.kiseru.checkers.service.JwtService

@Service
class JwtServiceImpl : JwtService {

    @Value("auth.secret")
    private lateinit var secret: String

    override fun generateAuthToken(username: String): String {
        val algorithm = Algorithm.HMAC256(secret)
        return JWT.create()
            .withSubject(username)
            .sign(algorithm)
    }
}