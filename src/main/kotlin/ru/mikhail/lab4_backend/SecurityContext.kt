package ru.mikhail.lab4_backend

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class SecurityContext {

    fun getCurrentUser(): String {

        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication != null && authentication.isAuthenticated) {
            val principal = authentication.principal
            if (principal is UserDetails) {
                principal.username
            } else {
                principal.toString()
            }
        } else {
            ""
        }
    }
}