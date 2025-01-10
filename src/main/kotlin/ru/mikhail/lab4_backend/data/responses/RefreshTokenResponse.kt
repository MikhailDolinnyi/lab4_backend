package ru.mikhail.lab4_backend.data.responses

data class RefreshTokenResponse(
    var accessToken: String? = "",
    var error: String = ""
)
