package ru.mikhail.lab4_backend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mikhail.lab4_backend.dbobjects.User
import ru.mikhail.lab4_backend.requests.LoginRequest
import ru.mikhail.lab4_backend.service.LoginService

@CrossOrigin(origins = ["http://localhost:3000"])

@RestController
@RequestMapping("/api")
class LoginController(private val loginService: LoginService) {

    @PostMapping("/login")
    fun registerUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<User> {
        val user = loginService.completeRequest(loginRequest) ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        return ResponseEntity(user, HttpStatus.OK)

    }


}