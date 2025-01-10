package ru.mikhail.lab4_backend.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.mikhail.lab4_backend.data.responses.GetDotsResponse
import ru.mikhail.lab4_backend.data.requests.CheckRequest
import ru.mikhail.lab4_backend.data.responses.CheckDotResponse
import ru.mikhail.lab4_backend.service.DotService




@RestController
@RequestMapping("/dot")
class DotController(private val dotService: DotService) {

    @PostMapping("/check")
    fun checkDot(@Valid @RequestBody checkRequest: CheckRequest): ResponseEntity<CheckDotResponse> {
        return dotService.completeCheckDot(checkRequest)

    }

    @GetMapping("/get-list")
    fun getResultList(): ResponseEntity<List<GetDotsResponse>> {
        val list = dotService.getList()
        return ResponseEntity(list, HttpStatus.OK)

    }

}