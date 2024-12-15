package ru.mikhail.lab4_backend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/secured")
class MainController {


    @GetMapping("/user")
    fun userAccess(principal: Principal):ResponseEntity<String>{
        return ResponseEntity(principal.name, HttpStatus.OK)
    }
}