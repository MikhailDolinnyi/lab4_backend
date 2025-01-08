package ru.mikhail.lab4_backend.responses

data class RefreshTokenResponse(
    var accessToken: String? = "",
    var error: String = ""
)
