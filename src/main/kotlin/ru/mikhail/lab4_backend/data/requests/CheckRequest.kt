package ru.mikhail.lab4_backend.data.requests

import com.fasterxml.jackson.annotation.JsonProperty


data class CheckRequest(
//    @field: Min(-5) @field:Max(3)
    @JsonProperty("x") val x: Float,

//    @field:Min(-3) @field:Max(3)
    @JsonProperty("y") val y: Float,

//    @field:Min(1) @field:Max(3)
    @JsonProperty("r") val r: Float
)
