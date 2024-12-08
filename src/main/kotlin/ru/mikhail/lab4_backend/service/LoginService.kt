package ru.mikhail.lab4_backend.service


import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.mikhail.lab4_backend.dbobjects.User
import ru.mikhail.lab4_backend.requests.LoginRequest
import ru.mikhail.lab4_backend.repository.UserRepository

@Service
class LoginService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()

    @Transactional
    fun completeRequest(loginRequest: LoginRequest): User? {

        var user = userRepository.findUserByUsername(loginRequest.username)

        if (user == null) {


            val hashedPassword = passwordEncoder.encode(loginRequest.password)
            user = User(username = loginRequest.username, password = hashedPassword)
            userRepository.save(user)

        }


        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            return null
        }

        return user

    }
}