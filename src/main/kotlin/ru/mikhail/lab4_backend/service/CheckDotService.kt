package ru.mikhail.lab4_backend.service

import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service
import ru.mikhail.lab4_backend.DotChecker.checkDot
import ru.mikhail.lab4_backend.dbobjects.Dot
import ru.mikhail.lab4_backend.repository.DotRepository
import ru.mikhail.lab4_backend.requests.CheckRequest
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class CheckDotService(
    private val dotRepository: DotRepository
) {


    companion object {
        private const val MIN_EXECUTION_TIME_NS = 1L
    }

    fun completeCheckDot(checkRequest: CheckRequest): Boolean {

        val startTime = System.nanoTime()
        val result = checkDot(checkRequest.x, checkRequest.y, checkRequest.r)
        val endTime = System.nanoTime()
        val executionTime = maxOf(endTime - startTime, MIN_EXECUTION_TIME_NS)
        val now = Timestamp.valueOf(LocalDateTime.now())

        val dot = Dot(
            x = checkRequest.x,
            y = checkRequest.y,
            r = checkRequest.r,
            result = result,
            executionTime = executionTime,
            time = now
        )

        return try {
            dotRepository.save(dot)
            true
        } catch (ex: DataAccessException) {
            println(ex.message)
            false
        }

    }


}