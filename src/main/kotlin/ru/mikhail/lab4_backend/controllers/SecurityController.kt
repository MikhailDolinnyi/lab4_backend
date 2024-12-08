package ru.mikhail.lab4_backend.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mikhail.lab4_backend.JwtCore
import ru.mikhail.lab4_backend.SignRequest
import ru.mikhail.lab4_backend.dbobjects.User
import ru.mikhail.lab4_backend.repository.UserRepository



@RequestMapping("/auth")
@RestController
class SecurityController(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtCore: JwtCore
) {


    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignRequest): Any {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name")
        }
        val hashed = passwordEncoder.encode(signUpRequest.password)
        val user = User(username = signUpRequest.username, password = hashed)
        userRepository.save(user)
        return ResponseEntity.status(HttpStatus.OK).body("Success registration")

    }

    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignRequest): Any {
        var authentication: Authentication? = null

        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    signInRequest.username,
                    signInRequest.password
                )
            )
        } catch (e: BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed")
        }catch (e: Exception){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e)
        }
        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String? = jwtCore.generateToken(authentication)
        return ResponseEntity.ok(jwt)

    }


}