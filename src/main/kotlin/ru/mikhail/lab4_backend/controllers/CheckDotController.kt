package ru.mikhail.lab4_backend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.mikhail.lab4_backend.dto.ResponseData
import ru.mikhail.lab4_backend.requests.CheckRequest
import ru.mikhail.lab4_backend.service.DotService


@CrossOrigin(origins = ["http://localhost:3000"])

@RestController
@RequestMapping("/dot")
class CheckDotController(private val dotService: DotService) {

    @PostMapping("/check")
    fun checkDot(@RequestBody checkRequest: CheckRequest): ResponseEntity<Boolean> {
        val response = dotService.completeCheckDot(checkRequest)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/get-list")
    fun getResultList(): ResponseEntity<List<ResponseData>> {
        val list = dotService.getList()
        return ResponseEntity(list, HttpStatus.OK)

    }

}