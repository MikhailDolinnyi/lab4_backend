package ru.mikhail.lab4_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mikhail.lab4_backend.dbobjects.User

interface LoginRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}