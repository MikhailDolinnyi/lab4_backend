package ru.mikhail.lab4_backend.requests

data class LoginRequest(
    val username: String,
    val password: String,
)
