package ru.mikhail.lab4_backend

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtCore {

    @Value("\${app.secret}")
    private lateinit var secret: String

    @Value("\${app.lifetime}")
    private var lifetime: Int = 0

    fun generateToken(authentication: Authentication): String? {
        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl

        return Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + lifetime))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun getNameFromJwt(token: String): String? {
        // Парсинг токена
        val claims: Claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body

        return claims.subject
    }
}
