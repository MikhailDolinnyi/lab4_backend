package ru.mikhail.lab4_backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.mikhail.lab4_backend.dbobjects.Dot

@Repository
interface DotRepository: JpaRepository<Dot, Long> {
}