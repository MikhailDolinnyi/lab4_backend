package ru.mikhail.lab4_backend.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/secured")
class MainController {


    @GetMapping("/user")
    public fun userAccess(principal: Principal): String? {
        return principal.name
    }
}