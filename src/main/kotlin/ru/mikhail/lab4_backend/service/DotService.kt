package ru.mikhail.lab4_backend.service

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.mikhail.lab4_backend.DotChecker.checkDot
import ru.mikhail.lab4_backend.DotMapper
import ru.mikhail.lab4_backend.SecurityContext
import ru.mikhail.lab4_backend.responses.GetDotsResponse
import ru.mikhail.lab4_backend.dbobjects.Dot
import ru.mikhail.lab4_backend.repository.DotRepository
import ru.mikhail.lab4_backend.requests.CheckRequest
import ru.mikhail.lab4_backend.responses.CheckDotResponse
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class DotService(
    private val dotRepository: DotRepository,
    private val dotMapper: DotMapper,
    private val securityContext: SecurityContext
) {




    companion object {
        private const val MIN_EXECUTION_TIME_NS = 1L
    }

    @Transactional
    fun completeCheckDot(checkRequest: CheckRequest): ResponseEntity<CheckDotResponse> {



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
                time = now,
                username = securityContext.getCurrentUser()

            )

            return try {
                dotRepository.save(dot)
                ResponseEntity(CheckDotResponse(info = "Dot successfully added!"), HttpStatus.OK)
            } catch (ex: DataAccessException) {
                println(ex.message)
                ResponseEntity(CheckDotResponse(error = "Error while trying connect to database"), HttpStatus.INTERNAL_SERVER_ERROR)
            }

    }

    @Transactional
    fun getList(): List<GetDotsResponse> {
        val dots = dotRepository.findDotsByUsername(securityContext.getCurrentUser())
        return dots.map { dotMapper.toDto(it) }
    }




}