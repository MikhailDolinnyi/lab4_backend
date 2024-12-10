package ru.mikhail.lab4_backend.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.mikhail.lab4_backend.authenticate.JwtCore
import ru.mikhail.lab4_backend.requests.SignRequest

@Service
class AuthService(
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtCore: JwtCore,
) {


    fun authorization(signInRequest: SignRequest): Any {
        val authentication: Authentication?

        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    signInRequest.username,
                    signInRequest.password
                )
            )
        } catch (e: BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed")
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e)
        }
        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String? = jwtCore.generateToken(authentication)
        return ResponseEntity.ok(jwt)


    }

}