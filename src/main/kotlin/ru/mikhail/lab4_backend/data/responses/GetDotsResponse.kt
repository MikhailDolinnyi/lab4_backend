package ru.mikhail.lab4_backend.data.responses

import java.sql.Timestamp

data class GetDotsResponse(
    var x: Float,
    var y: Float,
    var r: Float,

    var result: Boolean,
    var executionTime: Long,
    var time: Timestamp
)
