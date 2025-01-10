package ru.mikhail.lab4_backend.data.responses

data class SignInResponse(
    var username: String = "",
    var accessToken: String? = "",
    var refreshToken: String? = "",
    var error: String? = ""
) {
}