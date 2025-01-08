package ru.mikhail.lab4_backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mikhail.lab4_backend.authenticate.JwtCore
import ru.mikhail.lab4_backend.authenticate.UserDetailsImpl
import ru.mikhail.lab4_backend.repository.UserRepository
import ru.mikhail.lab4_backend.requests.RefreshTokenRequest
import ru.mikhail.lab4_backend.requests.SignRequest
import ru.mikhail.lab4_backend.responses.RefreshTokenResponse
import ru.mikhail.lab4_backend.responses.SignInResponse
import java.time.LocalDateTime

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtCore: JwtCore,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {


    @Value("\${refresh_lifetime}")
    private var refreshLifetime: Long = 0

    @Transactional
    fun authorization(signInRequest: SignRequest): ResponseEntity<SignInResponse> {
        val authentication: Authentication

        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    signInRequest.username,
                    signInRequest.password
                )
            )
        } catch (e: Exception) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(SignInResponse(eror = "Invalid username or password"))
            return ResponseEntity(SignInResponse(error = "Invalid username or password"), HttpStatus.UNAUTHORIZED)
        }


        val userDetails = authentication.principal as UserDetailsImpl

        val refreshToken: String?
        if (userDetails.getRefreshTokenHash() == null || userDetails.getRefreshTokenExpireTime()!!
                .isBefore(LocalDateTime.now())
        ) {
            refreshToken = jwtCore.generateRefreshToken(userDetails.username)
            val user = userRepository.getReferenceById(userDetails.getId())
            user.refreshTokenHash = passwordEncoder.encode(refreshToken)
            user.refreshTokenExpireTime = LocalDateTime.now().plusSeconds(refreshLifetime / 1000)
            userRepository.save(user)

        } else {
            refreshToken = userDetails.getRefreshTokenHash()
        }


        val accessToken = jwtCore.generateAccessToken(authentication)

        println(accessToken)
        println(refreshToken)


//        return ResponseEntity.ok(
//            mapOf(
//                "username" to userDetails.username,
//                "accessToken" to accessToken,
//                "refreshTokenHash" to refreshToken
//            )
//        )

        return ResponseEntity(SignInResponse(userDetails.username, accessToken, refreshToken), HttpStatus.OK)
    }


    @Transactional
    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {

        val incomingRefreshToken = refreshTokenRequest.refreshToken

        val username = jwtCore.getNameFromToken(incomingRefreshToken, isRefresh = true)
            ?: return ResponseEntity(RefreshTokenResponse(error = "Invalid refresh token"), HttpStatus.UNAUTHORIZED)

        val user = userRepository.findUserByUsername(username)
            ?: return ResponseEntity(RefreshTokenResponse(error = "User not found"), HttpStatus.UNAUTHORIZED)

        if (user.refreshTokenExpireTime!!.isBefore(LocalDateTime.now())) {
            return ResponseEntity(RefreshTokenResponse(error = "Refresh token expired"), HttpStatus.UNAUTHORIZED)
        }

        if (!passwordEncoder.matches(incomingRefreshToken, user.refreshTokenHash)) {
            return ResponseEntity(RefreshTokenResponse(error = "Invalid refresh token"), HttpStatus.UNAUTHORIZED)
        }

        val userDetails = UserDetailsImpl.build(user)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        val newAccessToken = jwtCore.generateAccessToken(authentication)


        return ResponseEntity(RefreshTokenResponse(accessToken = newAccessToken), HttpStatus.OK)


    }


}