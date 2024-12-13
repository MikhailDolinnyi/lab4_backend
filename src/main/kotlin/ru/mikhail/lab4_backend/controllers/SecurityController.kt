package ru.mikhail.lab4_backend.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mikhail.lab4_backend.requests.SignRequest
import ru.mikhail.lab4_backend.service.AuthService
import ru.mikhail.lab4_backend.service.UserService


@RequestMapping("/auth")
@RestController
class SecurityController(
    private val userService: UserService,
    private val authService: AuthService
) {


    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignRequest): Any {
        return userService.registerUser(signUpRequest)


    }

    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignRequest): Any {
        return authService.authorization(signInRequest)
    }


}