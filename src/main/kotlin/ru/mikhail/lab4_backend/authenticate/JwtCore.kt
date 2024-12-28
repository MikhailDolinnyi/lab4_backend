package ru.mikhail.lab4_backend.authenticate

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtCore {

    @Value("\${access_secret}")
    private lateinit var accessSecret: String

    @Value("\${access.lifetime}")
    private var accessLifetime: Long = 0

    @Value("\${refresh_secret}")
    private lateinit var refreshSecret: String

    @Value("\${refresh_lifetime}")
    private var refreshLifetime: Long = 0

    fun generateAccessToken(authentication: Authentication): String? {
        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl

        return Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + accessLifetime))
            .signWith(SignatureAlgorithm.HS256, accessSecret)
            .compact()
    }


    fun generateRefreshToken(username: String): String? {
        return Jwts.builder().setSubject(username).setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + refreshLifetime))
            .signWith(SignatureAlgorithm.HS256, refreshSecret).compact()
    }




    fun getNameFromToken(token: String, isRefresh: Boolean): String? {
        // Парсинг токена

        val claims: Claims = Jwts.parser()
            .setSigningKey(if (isRefresh) refreshSecret else accessSecret)
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

}
