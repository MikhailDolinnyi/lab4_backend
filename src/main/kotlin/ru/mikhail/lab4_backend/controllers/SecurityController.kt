package ru.mikhail.lab4_backend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mikhail.lab4_backend.data.requests.RefreshTokenRequest
import ru.mikhail.lab4_backend.data.requests.SignRequest
import ru.mikhail.lab4_backend.data.responses.RefreshTokenResponse
import ru.mikhail.lab4_backend.data.responses.SignInResponse
import ru.mikhail.lab4_backend.data.responses.SignUpResponse
import ru.mikhail.lab4_backend.service.AuthService
import ru.mikhail.lab4_backend.service.UserService


@RequestMapping("/auth")
@RestController
class SecurityController(
    private val userService: UserService,
    private val authService: AuthService
) {


    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignRequest): ResponseEntity<SignUpResponse> {
        return userService.registerUser(signUpRequest)


    }

    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignRequest): ResponseEntity<SignInResponse> {
        return authService.authorization(signInRequest)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        return authService.refreshToken(refreshTokenRequest)


    }


}