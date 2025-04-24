package ru.mikhail.lab4_backend.authenticate

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

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
        val path = request.servletPath
        if (path.startsWith("/auth/") ) {
            filterChain.doFilter(request, response)
            return
        }

        val headerAuth = request.getHeader("Authorization")
        val jwt = headerAuth?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (jwt != null) {
            try {
                val username = jwtCore.getNameFromToken(jwt, isRefresh = false)

                if (username != null && SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = auth
                } else{
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Invalid or expired token")
                    return
                }
            } catch (e: ExpiredJwtException) {
                // Если токен истек, возвращаем HTTP 401
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Access token expired")
                return
            } catch (e: Exception) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Invalid token")
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}
