package ru.mikhail.lab4_backend.dbobjects

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "dots")
data class Dot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var x: Float,
    var y: Float,
    var r: Float,

    var result: Boolean,
    var executionTime: Long,
    var time: Timestamp
)

