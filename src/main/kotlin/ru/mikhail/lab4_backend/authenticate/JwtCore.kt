package ru.mikhail.lab4_backend.authenticate

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtCore {

    private val logger: org.slf4j.Logger = LoggerFactory.getLogger(JwtCore::class.java)

    @Value("\${access_secret}")
    private lateinit var accessSecret: String

    @Value("\${access.lifetime}")
    private var accessLifetime: Long = 0

    @Value("\${refresh_secret}")
    private lateinit var refreshSecret: String

    @Value("\${refresh_lifetime}")
    private var refreshLifetime: Long = 0

    fun generateToken(authentication: Authentication, isRefresh: Boolean = false): String? {
        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl

        return Jwts.builder()
            .setSubject(userDetails.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + if (isRefresh) refreshLifetime else accessLifetime))
            .signWith(SignatureAlgorithm.HS256, if (isRefresh) refreshSecret else accessSecret)
            .compact()
    }





    fun getNameFromToken(token: String, isRefresh: Boolean): String? {
        // Парсинг токена
        return try {
            val claims: Claims = Jwts.parser()
                .setSigningKey(if (isRefresh) refreshSecret else accessSecret)
                .parseClaimsJws(token)
                .body

            claims.subject
        } catch (e: ExpiredJwtException) {
            // Логируем на уровне INFO или WARN
            logger.info("Token expired: ${e.message}")
            null
        } catch (e: JwtException) {
            // Логируем некорректные токены
            logger.warn("Invalid token: ${e.message}")
            null
        } catch (e: Exception) {
            // Логируем другие неожиданные ошибки
            logger.error("Unexpected error while parsing token", e)
            null
        }

    }

}
