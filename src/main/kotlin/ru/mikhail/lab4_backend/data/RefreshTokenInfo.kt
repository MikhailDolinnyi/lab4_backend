package ru.mikhail.lab4_backend.data

import java.time.LocalDateTime

data class RefreshTokenInfo(
    var refreshToken: String?,
    var refreshTokenExpireTime: LocalDateTime
)
