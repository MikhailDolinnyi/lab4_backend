package ru.mikhail.lab4_backend.responses

data class SignInResponse(
    var username: String = "",
    var accessToken: String? = "",
    var refreshToken: String? = "",
    var error: String = ""
) {
}