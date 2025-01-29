package ru.mikhail.lab4_backend.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Dot Controller", description = "Контроллер для работы с точками")
class DotController(private val dotService: DotService) {

    @Operation(summary = "Проверка точки", description = "Выполняет проверку попадания точки в область")
    @PostMapping("/check")
    fun checkDot(@Valid @RequestBody checkRequest: CheckRequest): ResponseEntity<CheckDotResponse> {
        return dotService.completeCheckDot(checkRequest)

    }

    @Operation(summary = "Получение списка точек", description = "Возвращает список всех точек")
    @GetMapping("/get-list")
    fun getResultList(): ResponseEntity<List<GetDotsResponse>> {
        val list = dotService.getList()
        return ResponseEntity(list, HttpStatus.OK)

    }

}