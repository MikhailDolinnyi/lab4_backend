package ru.mikhail.lab4_backend.dto

import java.sql.Timestamp

data class ResponseData(
    var x: Float,
    var y: Float,
    var r: Float,

    var result: Boolean,
    var executionTime: Long,
    var time: Timestamp
)
