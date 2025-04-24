package ru.mikhail.lab4_backend.mbeans

interface DotStatsMBean {

    fun getTotalDots(): Int
    fun getMissedDots(): Int
}