package ru.mikhail.lab4_backend.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.mikhail.lab4_backend.authenticate.JwtCore
import ru.mikhail.lab4_backend.data.RefreshTokenInfo
import ru.mikhail.lab4_backend.authenticate.UserDetailsImpl
import ru.mikhail.lab4_backend.repository.UserRepository
import ru.mikhail.lab4_backend.data.requests.RefreshTokenRequest
import ru.mikhail.lab4_backend.data.requests.SignRequest
import ru.mikhail.lab4_backend.data.responses.RefreshTokenResponse
import ru.mikhail.lab4_backend.data.responses.SignInResponse
import java.time.LocalDateTime

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtCore: JwtCore,
    private val userRepository: UserRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
    @Qualifier("objectMapper") private val objectMapper: ObjectMapper
) {

    private val logger: org.slf4j.Logger = LoggerFactory.getLogger(JwtCore::class.java)

    @Value("\${refresh_lifetime}")
    private var refreshLifetime: Long = 0


    fun authorization(signInRequest: SignRequest): ResponseEntity<SignInResponse> {
        val authentication: Authentication

        try {
            authentication = authenticate(signInRequest)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity(SignInResponse(error = e.message), HttpStatus.UNAUTHORIZED)
        }

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.principal as UserDetailsImpl

        val refreshToken = jwtCore.generateToken(authentication, true)

        val refreshTokenExpireTime = LocalDateTime.now().plusSeconds(refreshLifetime / 1000)



        redisTemplate.opsForValue().set(
            userDetails.username,
            RefreshTokenInfo(refreshToken, refreshTokenExpireTime)
        )

        val accessToken = jwtCore.generateToken(authentication)

        println(accessToken)



        return ResponseEntity(SignInResponse(userDetails.username, accessToken, refreshToken), HttpStatus.OK)
    }


    fun authenticate(signInRequest: SignRequest): Authentication {
        return try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    signInRequest.username,
                    signInRequest.password
                )
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid username or password", e)
        }
    }


    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {

        val incomingRefreshToken = refreshTokenRequest.refreshToken

        val username = jwtCore.getNameFromToken(incomingRefreshToken, isRefresh = true)
            ?: return ResponseEntity(
                RefreshTokenResponse(error = "Invalid or expired incoming refresh token"),
                HttpStatus.UNAUTHORIZED
            )

        val user = userRepository.findUserByUsername(username)
            ?: return ResponseEntity(RefreshTokenResponse(error = "User not found"), HttpStatus.UNAUTHORIZED)

        val storedTokenInfo = redisTemplate.opsForValue().get(username)
            ?.let { value -> objectMapper.convertValue(value, RefreshTokenInfo::class.java) }


        if (storedTokenInfo == null || storedTokenInfo.refreshTokenExpireTime.isBefore(LocalDateTime.now())) {
            return ResponseEntity(
                RefreshTokenResponse(error = "Refresh token expired or not found"),
                HttpStatus.UNAUTHORIZED
            )
        }

        if (incomingRefreshToken != storedTokenInfo.refreshToken) {
            return ResponseEntity(RefreshTokenResponse(error = "Invalid refresh token"), HttpStatus.UNAUTHORIZED)
        }


        val userDetails = UserDetailsImpl.build(user)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        val newAccessToken = jwtCore.generateToken(authentication)




        return ResponseEntity(RefreshTokenResponse(accessToken = newAccessToken), HttpStatus.OK)


    }


}