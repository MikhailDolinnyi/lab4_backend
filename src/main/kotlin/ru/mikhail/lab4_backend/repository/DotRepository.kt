package ru.mikhail.lab4_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mikhail.lab4_backend.dbobjects.Dot

interface DotRepository: JpaRepository<Dot, Long> {
}