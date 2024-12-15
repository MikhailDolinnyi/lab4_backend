package ru.mikhail.lab4_backend.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.mikhail.lab4_backend.dto.ResponseData
import ru.mikhail.lab4_backend.requests.CheckRequest
import ru.mikhail.lab4_backend.service.DotService




@RestController
@RequestMapping("/dot")
class CheckDotController(private val dotService: DotService) {

    @PostMapping("/check")
    fun checkDot(@Valid @RequestBody checkRequest: CheckRequest): ResponseEntity<String> {
        return dotService.completeCheckDot(checkRequest)

    }

    @GetMapping("/get-list")
    fun getResultList(): ResponseEntity<List<ResponseData>> {
        val list = dotService.getList()
        return ResponseEntity(list, HttpStatus.OK)

    }

}