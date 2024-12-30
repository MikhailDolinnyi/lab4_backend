package ru.mikhail.lab4_backend.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mikhail.lab4_backend.authenticate.UserDetailsImpl
import ru.mikhail.lab4_backend.dbobjects.User
import ru.mikhail.lab4_backend.repository.UserRepository
import ru.mikhail.lab4_backend.requests.SignRequest


@Service
class UserService(
    private var userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {


    @Transactional
    fun registerUser(signUpRequest: SignRequest): ResponseEntity<String> {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name")
        }
        val hashed = passwordEncoder.encode(signUpRequest.password)
        val user = User(username = signUpRequest.username, password = hashed)
        userRepository.save(user)
        return ResponseEntity.status(HttpStatus.OK).body("Success registration")
    }

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByUsername(username)
            ?: throw UsernameNotFoundException("User '$username' not found")
        return UserDetailsImpl.build(user)
    }


}