package ru.mikhail.lab4_backend.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.mikhail.lab4_backend.UserDetailsImpl
import ru.mikhail.lab4_backend.dbobjects.User
import ru.mikhail.lab4_backend.repository.UserRepository


@Service
class UserService(@Autowired private var userRepository: UserRepository): UserDetailsService {







    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByUsername(username)
            ?: throw UsernameNotFoundException("User '$username' not found")
        return UserDetailsImpl.build(user)
    }



}