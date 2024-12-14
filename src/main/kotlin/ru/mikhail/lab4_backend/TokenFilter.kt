package ru.mikhail.lab4_backend

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.mikhail.lab4_backend.authenticate.JwtCore

@Component
class TokenFilter(
    private val jwtCore: JwtCore,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val headerAuth = request.getHeader("Authorization")
        val jwt = headerAuth?.takeIf { it.startsWith("Bearer ") }?.substring(7)
        if (jwt != null) {
            try {
                val username = jwtCore.getNameFromJwt(jwt)
                if (username != null && SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: ExpiredJwtException) {
                //TODO
            }
        }
        filterChain.doFilter(request, response)
    }
}
