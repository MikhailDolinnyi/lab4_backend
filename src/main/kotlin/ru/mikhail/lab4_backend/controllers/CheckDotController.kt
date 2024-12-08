package ru.mikhail.lab4_backend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.mikhail.lab4_backend.requests.CheckRequest
import ru.mikhail.lab4_backend.service.CheckDotService


@CrossOrigin(origins = ["http://localhost:3000"])

@RestController
@RequestMapping("/api")
class CheckDotController (private val checkDotService: CheckDotService){

    @PostMapping("/check-dot")
    fun checkDot(@RequestBody checkRequest: CheckRequest): ResponseEntity<Boolean> {
        val response = checkDotService.completeCheckDot(checkRequest)

        return ResponseEntity(response, HttpStatus.OK)
    }
}