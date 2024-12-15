package ru.mikhail.lab4_backend.service

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.mikhail.lab4_backend.DotChecker.checkDot
import ru.mikhail.lab4_backend.DotMapper
import ru.mikhail.lab4_backend.dto.ResponseData
import ru.mikhail.lab4_backend.dbobjects.Dot
import ru.mikhail.lab4_backend.repository.DotRepository
import ru.mikhail.lab4_backend.requests.CheckRequest
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.xml.bind.ValidationException

@Service
class DotService(
    private val dotRepository: DotRepository,
    private val dotMapper: DotMapper
) {


    companion object {
        private const val MIN_EXECUTION_TIME_NS = 1L
    }

    fun completeCheckDot(checkRequest: CheckRequest): ResponseEntity<String> {

//        try {
//            isValid(checkRequest)

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
                ResponseEntity("Dot successfully added!", HttpStatus.OK)
            } catch (ex: DataAccessException) {
                println(ex.message)
                ResponseEntity("Error while trying connect to database", HttpStatus.INTERNAL_SERVER_ERROR)
            }

//        } catch (e: ValidationException) {
//            println(e.message)
//            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
//
//        }
    }

    fun getList(): List<ResponseData> {
        val dots = dotRepository.findAll()
        return dots.map { dotMapper.toDto(it) }
    }




}